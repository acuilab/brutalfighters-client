package com.brutalfighters.game.utility;

import java.awt.Rectangle;

public class CollisionDetection {
	public static Rectangle getBounds(String flip, int x, int y, int width, int height) {
		if(flip.equals("left")) { //$NON-NLS-1$
			return new Rectangle(x-width,y-height/2,width,height);
		} else if(flip.equals("right")) { //$NON-NLS-1$
			return new Rectangle(x,y-height/2,width,height);
		} else if(flip.equals("both")) { //$NON-NLS-1$
			return new Rectangle(x-width/2,y-height/2,width,height);
		} else {
			return null;
		}
	}
	
	public static Rectangle getBounds(String flip, float x, float y, int width, int height) {
		return getBounds(flip, (int)x, (int)y, width, height);
	}
}
