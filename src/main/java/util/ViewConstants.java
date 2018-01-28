/* CALCULATED RISK
 * 
 * KEVIN O'BRIEN 12498432
 * EOIN BYRNE 	 14333046
 * TOM SLATTERY  11526347
 */

//Constants to be used to organise the view.
//All dimensions/sizes/coordinates are calculated so that they scale
//to the display size on which the game is being played

package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class ViewConstants {
	
	public static final Dimension WINDOW_DIMENSION = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WINDOW_WIDTH = (int) (WINDOW_DIMENSION.getWidth());
	public static final int WINDOW_HEIGHT = (int) (WINDOW_DIMENSION.getHeight());

	public static final int MAP_HEIGHT = WINDOW_HEIGHT;
	public static final int MAP_WIDTH = (int) (WINDOW_WIDTH / 1.185);
	public static final Dimension MAP_DIMENSION = getDimension(MAP_WIDTH, MAP_HEIGHT);
	public static final int MAP_X_COORD = 0;
	public static final int MAP_Y_COORD = 0;
	
	public static final int COUNTRY_CIRCLE_DIAMETER = (MAP_WIDTH / 35);
	public static final Color BROWN = new Color(107,69, 38);
	public static final Font COUNTRY_LABEL_FONT = new Font("MS Sans Serif", Font.BOLD, (int) (MAP_WIDTH / 104));
	public static final int COUNTRY_LABEL_WIDTH = (int) (MAP_WIDTH / 11.87);
	public static final int COUNTRY_LABEL_HEIGHT = (int) (MAP_WIDTH / 57.6);
	
	public static final int CONTINENT_KEY_PANEL_WIDTH = MAP_WIDTH / 6;
	public static final int CONTINENT_KEY_PANEL_HEIGHT = (int) (WINDOW_HEIGHT / 2.84);
	public static final int CONTINENT_KEY_PANEL_X_COORD = MAP_WIDTH / 136;
	public static final int CONTINENT_KEY_PANEL_Y_COORD = MAP_HEIGHT - CONTINENT_KEY_PANEL_HEIGHT - CONTINENT_KEY_PANEL_X_COORD;
	
	public static final int CONTINENT_COLOR_PANEL_WIDTH = CONTINENT_KEY_PANEL_WIDTH;
	public static final int CONTINENT_COLOR_PANEL_HEIGHT = CONTINENT_KEY_PANEL_HEIGHT / 6;
	public static final int CONTINENT_COLOR_PANEL_X_COORD = 0;
	public static final int CONTINENT_COLOR_PANEL_Y_COORD = CONTINENT_COLOR_PANEL_HEIGHT;
	
	public static final int CONTINENT_COLOR_CIRCLE_WIDTH = COUNTRY_CIRCLE_DIAMETER;
	public static final int CONTINENT_COLOR_CIRCLE_HEIGHT = CONTINENT_COLOR_CIRCLE_WIDTH;
	public static final int CONTINENT_COLOR_CIRCLE_X_COORD = WINDOW_HEIGHT / 128;
	public static final int CONTINENT_COLOR_CIRCLE_Y_COORD = WINDOW_HEIGHT / 128;

	public static final int CONTINENT_LABEL_WIDTH = CONTINENT_COLOR_PANEL_WIDTH - COUNTRY_CIRCLE_DIAMETER;
	public static final int CONTINENT_LABEL_HEIGHT = CONTINENT_COLOR_PANEL_HEIGHT;
	public static final int CONTINENT_LABEL_X_COORD = (int) (COUNTRY_CIRCLE_DIAMETER * 1.4);
	public static final int CONTINENT_LABEL_Y_COORD = 0;
	public static final Font CONTINENT_LABEL_FONT = new Font("MS Sans Serif", Font.PLAIN, (int) (MAP_WIDTH / 57.6));
	
	public static final Color[] CONTINENT_COLORS = {Color.ORANGE, Color.LIGHT_GRAY, Color.GREEN, Color.MAGENTA, Color.RED, BROWN};
	public static final Color[] PLAYER_COLORS = {Color.CYAN, Color.YELLOW, Color.PINK, Color.BLUE, Color.GRAY, Color.WHITE};
	public static final String[] PLAYER_COLOR_NAMES = {"Cyan", "Yellow", "Pink", "Blue", "Gray", "White"};
	
	public static final int SIDEBAR_PANEL_WIDTH = WINDOW_WIDTH - MAP_WIDTH - (WINDOW_HEIGHT / 64);
	public static final int SIDEBAR_PANEL_HEIGHT = WINDOW_HEIGHT;
	public static final int SIDEBAR_PANEL_X_COORD = MAP_WIDTH + (WINDOW_WIDTH - MAP_WIDTH - SIDEBAR_PANEL_WIDTH) / 2;
	public static final int SIDEBAR_PANEL_Y_COORD = 0;
	public static final Font SIDEBAR_FONT = new Font("MS Sans Serif", Font.BOLD, WINDOW_HEIGHT / 64);
	
	public static final int EXIT_BUTTON_WIDTH = SIDEBAR_PANEL_WIDTH / 6;
	public static final int EXIT_BUTTON_HEIGHT = EXIT_BUTTON_WIDTH;
	public static final int EXIT_BUTTON_X_COORD = SIDEBAR_PANEL_WIDTH - EXIT_BUTTON_WIDTH;
	public static final int EXIT_BUTTON_Y_COORD = (WINDOW_HEIGHT / 256);
	
	public static final int LOGO_PANEL_WIDTH = SIDEBAR_PANEL_WIDTH;
	public static final int LOGO_PANEL_HEIGHT = LOGO_PANEL_WIDTH / 3;
	public static final int LOGO_PANEL_X_COORD = 0;
	public static final int LOGO_PANEL_Y_COORD = (WINDOW_HEIGHT / 48);
	
	public static final int PLAYER_PANEL_X_COORD = 0;
	public static final int PLAYER_PANEL_Y_COORD = (int) (WINDOW_HEIGHT / 8.7);
	public static final int PLAYER_PANEL_WIDTH = SIDEBAR_PANEL_WIDTH;
	public static final int PLAYER_PANEL_HEIGHT = (int) (WINDOW_HEIGHT / 5.647);
	
	public static final int LOG_PANEL_X_COORD = 0;
	public static final int LOG_PANEL_Y_COORD = (int) (WINDOW_HEIGHT / 3.3684);
	public static final int LOG_PANEL_WIDTH = SIDEBAR_PANEL_WIDTH;
	public static final int LOG_PANEL_HEIGHT = (int) (WINDOW_HEIGHT / 1.72584) + (WINDOW_HEIGHT / 19);
	public static final Font LOG_PANEL_FONT = new Font("MS Sans Serif", Font.PLAIN, WINDOW_HEIGHT / 64);
	
	public static final int COMMAND_PANEL_WIDTH = SIDEBAR_PANEL_WIDTH;
	public static final int COMMAND_PANEL_HEIGHT = WINDOW_HEIGHT - (LOG_PANEL_Y_COORD + LOG_PANEL_HEIGHT);
	public static final int COMMAND_PANEL_X_COORD = 0;
	public static final int COMMAND_PANEL_Y_COORD = (int) (LOG_PANEL_Y_COORD + LOG_PANEL_HEIGHT + (WINDOW_HEIGHT / 256));

	public static final int COMMAND_LABEL_WIDTH = COMMAND_PANEL_WIDTH;
	public static final int COMMAND_LABEL_HEIGHT = (WINDOW_HEIGHT / 38);
	public static final int COMMAND_LABEL_X_COORD = 0;
	public static final int COMMAND_LABEL_Y_COORD = 0;
	
	public static final int COMMAND_INPUT_FIELD_WIDTH = COMMAND_PANEL_WIDTH;
	public static final int COMMAND_INPUT_FIELD_HEIGHT = COMMAND_LABEL_HEIGHT;
	public static final int COMMAND_INPUT_FIELD_X_COORD = 0;
	public static final int COMMAND_INPUT_FIELD_Y_COORD = (WINDOW_HEIGHT / 31);
	
	public static final int PLAYER_COLOR_PANEL_WIDTH = (SIDEBAR_PANEL_WIDTH / 2);
	public static final int PLAYER_COLOR_PANEL_HEIGHT = (PLAYER_PANEL_HEIGHT / 3);
	public static final int PLAYER_COLOR_PANEL_LEFT_X_COORD = 0;
	public static final int PLAYER_COLOR_PANEL_RIGHT_X_COORD = (SIDEBAR_PANEL_WIDTH / 2);
	public static final int PLAYER_COLOR_PANEL_TOP_Y_COORD = 0;
	public static final int PLAYER_COLOR_PANEL_MIDDLE_Y_COORD = (PLAYER_PANEL_HEIGHT * 1/3);
	public static final int PLAYER_COLOR_PANEL_BOTTOM_Y_COORD = (PLAYER_PANEL_HEIGHT * 2/3);

	public static final int PLAYER_COLOR_CIRCLE_DIAMETER = (int) (COUNTRY_CIRCLE_DIAMETER / 2);
	public static final int PLAYER_COLOR_CIRCLE_X_COORD = (int) WINDOW_HEIGHT / 128;
	public static final int PLAYER_COLOR_CIRCLE_Y_COORD = PLAYER_COLOR_CIRCLE_X_COORD;
	
	public static final int PLAYER_COLOR_LABEL_WIDTH = PLAYER_COLOR_PANEL_WIDTH - PLAYER_COLOR_CIRCLE_DIAMETER;
	public static final int PLAYER_COLOR_LABEL_HEIGHT = PLAYER_COLOR_PANEL_HEIGHT;
	public static final int PLAYER_COLOR_LABEL_X_COORD = (int) (COUNTRY_CIRCLE_DIAMETER * 1.45);
	public static final int PLAYER_COLOR_LABEL_Y_COORD = 0;
	
	public static final int ACTIVE_PLAYER_CIRCLE_WIDTH = (int) (ViewConstants.COUNTRY_CIRCLE_DIAMETER * 1.22);
	public static final int ACTIVE_PLAYER_CIRCLE_HEIGHT = ACTIVE_PLAYER_CIRCLE_WIDTH;
	public static final int ACTIVE_PLAYER_CIRCLE_X_COORD = (int) (ViewConstants.PLAYER_COLOR_CIRCLE_X_COORD - 3);
	public static final int ACTIVE_PLAYER_CIRCLE_Y_COORD = (int) (ViewConstants.PLAYER_COLOR_CIRCLE_Y_COORD - 3);
	
	public static final Font NUMBER_OF_UNITS_FONT = new Font("MS Sans Serif", Font.BOLD, (int) (MAP_WIDTH / 80));
	
	public static final String PLAYER_ORDER = "player order";
	public static final String ATTACKING = "attacking";
	public static final String DEFENDING = "defending";
	
	public static final int DICE_FRAME_WIDTH = (int) (WINDOW_HEIGHT / 4.8);
	public static final int DICE_FRAME_X_COORD = (int) (WINDOW_WIDTH / 1.95); //700
	public static final int DICE_FRAME_Y_COORD = (int) (WINDOW_WIDTH / 2.6269); //520
	
	public static final int[] DICE_DIMENSIONS = {(int) (WINDOW_HEIGHT / 25), (int) (WINDOW_HEIGHT / 50), (int) (WINDOW_HEIGHT / 7.68), (int) (WINDOW_HEIGHT / 7.68)};

	public static final int[] ATTACKING_DICE_FRAME_X_COORD = {(int) (WINDOW_WIDTH / 1.95), (int) (WINDOW_WIDTH / 2.133), 765};
	public static final int[] ATTACKING_DICE_FRAME_Y_COORD = {(int) (WINDOW_WIDTH / 2.73), (int) (WINDOW_WIDTH / 2.15), 630};
	public static final int[] ATTACKING_DICE_ROTATIONS = {3, 6, 35};
	
	public static final int[] DEFENDING_DICE_FRAME_X_COORD = {(int) (WINDOW_WIDTH / 4.33), (int) (WINDOW_WIDTH / 3.5)};
	public static final int[] DEFENDING_DICE_FRAME_Y_COORD = {(int) (WINDOW_WIDTH / 2.529), (int) (WINDOW_WIDTH / 2.185)};
	public static final int[] DEFENDING_DICE_ROTATIONS = {4, 25};
	
	//public static final int PLAYER_PANEL_DISPLAY_NUMBER_X_COORD = 19;
	public static final int PLAYER_PANEL_DISPLAY_NUMBER_Y_COORD = 27;

	private static Dimension getDimension(int width, int height) {
		return new Dimension(width, height);
	}

}
