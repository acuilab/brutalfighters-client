package com.brutalfighters.game.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

public class GameMap {
	
	private static final String BLOCKED = "blocked"; //$NON-NLS-1$
	private static final String BLOCKED_TOP = "top"; //$NON-NLS-1$
	private static final String STEP = "step"; //$NON-NLS-1$
	private static final String TELEPORT = "teleport"; //$NON-NLS-1$
	private static final String RATIO = "ratio"; //$NON-NLS-1$
	
	private String mapName;
	
	private TiledMap map;
	
	private TiledMapTileLayer colLayer;
	
	private float cellWidth; // Float because LibGDX's Tiled has float width and height.
	private float cellHeight; // Float because LibGDX's Tiled has float width and height.
	
	protected int leftBoundary;
	protected int rightBoundary;
	protected int topBoundary;
	protected int botBoundary;
	
	private final float TILE_SCALE;
	
	private List<Teleport> teleports;
	
	public GameMap(String name, float scale) {
		mapName = name;
		
		TILE_SCALE = scale;
		
		LoadMap("maps/" + name + "/" + name + ".tmx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		teleports = new ArrayList<Teleport>();
		for(int y = 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				if(hasProperty(TELEPORT, x, y)) {
					// LibGDX library for Tiled has the function: `getCell(x,y)` it flips the Y, which means 0 will be the bottom,
					// unlike in Tiled or the map file, which is 0 there is the top of the map.
					// That's why we don't need to flip the position of the teleport, but we need to flip the destination of the teleport.
					String[] target = ((String) getCell(x,y).getTile().getProperties().get("teleport")).split(","); //$NON-NLS-1$ //$NON-NLS-2$
					teleports.add(new Teleport(toPixelX(x),toPixelY(y), toPixelX(Integer.parseInt(target[0])), (int) getHeightSize() - toPixelY(Integer.parseInt(target[1]))));
				}
			}
		}
	}
	
	public void LoadMap(String path) {
		System.out.println("Searching and locating the map to load it!"); //$NON-NLS-1$
		
		map = new TmxMapLoader().load(path);
		
		colLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
		cellWidth = colLayer.getTileWidth();
		cellHeight = colLayer.getTileHeight();
		
		this.setLeftBoundary(5);
		this.setRightBoundary((int)getWidthSize()-5);
		
		this.setBotBoundary(5);
		this.setTopBoundary((int)getHeightSize()-5);
	}
	
	public TiledMap getMap() {
		return map;
	}
	public String getName() {
		return mapName;
	}
	public TiledMapTileLayer getColLayer() {
		return colLayer;
	}
	public Cell getCell(int x, int y) {
		return colLayer.getCell(x, y);
	}
	public Cell getCell(float x, float y) {
		return colLayer.getCell(toCellX(x), toCellY(y));
	}
	public boolean isBlocked(String blocked, int x, int y) {
		return getProperty(BLOCKED(), x, y).equals(blocked);
	}
	public boolean isBlocked(String blocked, float x, float y) {
		return getProperty(BLOCKED(), x, y).equals(blocked);
	}
	public boolean isBlocked(int x, int y) {
		return isBlocked(BLOCKED(), x, y);
	}
	public boolean isBlocked(float x, float y) {
		return isBlocked(BLOCKED(), x, y);
	}
	public float getTileWidth() {
		return cellWidth * getScaled();
	}
	public float getTileHeight() {
		return cellHeight * getScaled();
	}
	public float getWidthSize() {
		return getWidth() * getTileWidth();
	}
	public float getHeightSize() {
		return getHeight() * getTileHeight();
	}
	public float getWidth() {
		return colLayer.getWidth();
	}
	public float getHeight() {
		return colLayer.getHeight();
	}
	public int toCellX(float x) {
		return (int) (x / getTileWidth());
	}
	public int toCellY(float y) {
		return (int) (y / getTileHeight());
	}
	public int toPixelX(float x) {
		return (int) ((x) * getTileWidth() + getTileWidth()/2);
	}
	public int toPixelY(float y) {
		return (int) ((y) * getTileHeight() + getTileHeight()/2);
	}
	
	// Boundaries
	public int getRightBoundary() {
		return rightBoundary;
	}
	public void setRightBoundary(int rightBoundary) {
		this.rightBoundary = rightBoundary;
	}
	public int getLeftBoundary() {
		return leftBoundary;
	}
	public void setLeftBoundary(int leftBoundary) {
		this.leftBoundary = leftBoundary;
	}
	
	public int getTopBoundary() {
		return topBoundary;
	}
	public void setTopBoundary(int topBoundary) {
		this.topBoundary = topBoundary;
	}
	public int getBotBoundary() {
		return botBoundary;
	}
	public void setBotBoundary(int botBoundary) {
		this.botBoundary = botBoundary;
	}
	
	public String getProperty(String key, int x, int y) {
		return hasProperty(key, getCell(x,y)) ? (String)getCell(x,y).getTile().getProperties().get(key) : "none"; //$NON-NLS-1$
	}
	public String getProperty(String key, float x, float y) {
		return hasProperty(key, getCell(x,y)) ? (String)getCell(x,y).getTile().getProperties().get(key) : "none"; //$NON-NLS-1$
	}
	public static String getProperty(String key, Cell cell) {
		return hasProperty(key, cell) ? cell.getTile().getProperties().get(key).toString() : "none"; //$NON-NLS-1$
	}
	
	public boolean hasProperty(String key, String value, int x, int y) {
		return getProperty(key, x, y).equals(value);
	}
	public boolean hasProperty(String key, String value, float x, float y) {
		return getProperty(key, x, y).equals(value);
	}
	public static boolean hasProperty(String key, String value, Cell cell) {
		return getProperty(key, cell).equals(value);
	}
	
	public boolean hasProperty(String key, int x, int y) {
		return hasProperty(key, getCell(x,y));
	}
	public boolean hasProperty(String key, float x, float y) {
		return hasProperty(key, getCell(x,y));
	}
	public static boolean hasProperty(String key, Cell cell) {
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(key);
	}
	
	public float getScaled() {
		return TILE_SCALE;
	}
	
	public List<Teleport> getTeleports() {
		return teleports;
	}
	
	public int alignTiledX(int posx) {
		return (posx/(int)getTileWidth()) * (int)getTileWidth();
	}
	public int alignTiledY(int posy) {
		return (posy/(int)getTileHeight()) * (int)getTileHeight();
	}
	
	// Collision Detection
	public Rectangle getBounds(float x, float y) {
		Rectangle bounds = new Rectangle(alignTiledX((int)x), alignTiledY((int)y), (int)getTileWidth(), (int)getTileHeight());
		return bounds;
	}
	public boolean intersect(float x, float y, Rectangle bounds) {
		return getBounds(x,y).overlaps(bounds);
	}
	
	// Collision Detection
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.
	 */
	public boolean intersects(String blocked, float x, float y, Rectangle bounds) {
		
		Cell cell = getCell(x,y);
		
		if(isBlocked(blocked, x,y) && (((getTileHeight()-y%getTileHeight()) / getTileHeight()) <= Float.parseFloat(cell.getTile().getProperties().get(RATIO()).toString()))) {
			//return intersect(0,toCellX(x),toCellY(y),bounds); //AABB Implemented but not needed :`(
			return true;
		}
		return false;
	}
	public boolean intersectsSurroundX(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x-bounds.width/2+1,y, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x+bounds.width/2-1,y, bounds);
	}
	public boolean intersectsSurroundY(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x,y-bounds.height/2+1, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x,y+bounds.height/2-1, bounds);
	}
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.
	 */
	public boolean intersects(float x, float y, Rectangle bounds) {
		return intersects(BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundX(float x, float y, Rectangle bounds) {
		return intersectsSurroundX(BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundY(float x, float y, Rectangle bounds) {
		return intersectsSurroundY(BLOCKED(), x, y, bounds);
	}
	
	public boolean intersectsSurroundXBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundX(BLOCKED(), x, y, bounds) || intersectsSurroundX(blocked, x, y, bounds);
	}
	public boolean intersectsSurroundYBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundY(BLOCKED(), x, y, bounds) || intersectsSurroundY(blocked, x, y, bounds);
	} 
	
	public static String BLOCKED() {
		return BLOCKED;
	}
	public static String BLOCKED_TOP() {
		return BLOCKED_TOP;
	}
	public static String STEP() {
		return STEP;
	}
	public static String TELEPORT() {
		return TELEPORT;
	}
	public static String RATIO() {
		return RATIO;
	}

	public void dispose() {
		System.out.println("Disposing The Map"); //$NON-NLS-1$
		map.dispose();
	}
}
