package edu.wisc.ssec.mcidasv;

/**
 * Application wide constants.
 * @version $Id$
 */
public interface Constants {

	/** Application property file name. */
	public static final String PROPERTIES_FILE = 
		"/edu/wisc/ssec/mcidasv/resources/mcidasv.properties";
	
	/** Specifies use of {@link edu.wisc.ssec.mcidasv.ui.TabbedUIManager}. */
	public static final String PROP_TABBED_UI = "mcidasv.tabbedDisplay";
	
	/** Property name for for the path to about dialog template. */
	public static String PROP_ABOUTTEXT = "mcidasv.about.text";
	/** Property name for the McIdas-V homepage URL. */
	public static String PROP_HOMEPAGE = "mcidasv.homepage";
	/** Property name for the path to version file. */
	public static String PROP_VERSIONFILE = "mcidasv.version.file";
	/** Property name for the major version number. */
	public static String PROP_VERSION_MAJOR = "mcidasv.version.major";
	/** Property name for the minor version number. */
	public static String PROP_VERSION_MINOR = "mcidasv.version.minor";
	/** Property name for the version release number. */
	public static String PROP_VERSION_RELEASE = "mcidasv.version.release";
	/** Property name for the copyright year. */
	public static String PROP_COPYRIGHT_YEAR = "mcidasv.copyright.year";
	
	/** Macro for the copyright year in the about HTML file. */
	public static String MACRO_COPYRIGHT_YEAR = "%COPYRIGHT_YEAR%";
	/** Macro for the version in the about HTML file. */
	public static String MACRO_VERSION = "%MCVERSION%";
	/** Macro for the IDV version in the about HTML file. */
	public static String MACRO_IDV_VERSION = "%IDVVERSION%";
	
	/** Enabled the toolbar manipulation JPopupMenu. */
	public static final String PREF_TBM_ENABLED ="tbm.enabled";
	
	/** 
	 * Show large or small icons. If PREF_TBM_ICONS is disabled, this pref
	 * has no meaning.
	 */
	public static final String PREF_TBM_SIZE = "tbm.icon.size";
	
	/** Whether or not icons should be shown in the toolbar. */
	public static final String PREF_TBM_ICONS = "tbm.icon.enabled";
	
	/** Whether or not labels should be shown in the toolbar. */
	public static final String PREF_TBM_LABELS = "tbm.label.enabled";
	
	/** The toolbar display option that was chosen. */
	public static final String PREF_TBM_SELOPT = "tbm.bg.selected";

	/** The location of the actions file. */
	public static final String PROP_TBM_ACTIONS = "mcidasv.apptoolbar.actions";
	
	/** The location of the file with the large icon actions. */
	public static final String PROP_TBM_LARGE = 
		"mcidasv.apptoolbar.largeactions";
	
	/** The location of the file with the small icon actions. */
	public static final String PROP_TBM_SMALL = 
		"mcidasv.apptoolbar.smallactions";
	
	
	
	/**
	 * The name of thing that contains the actual VisAD display,
	 * the animation control, view and projection menus, and the
	 * toolbar.
	 */
	public static final String PANEL_NAME = "Panel";
	
	/**
	 * A thing that contains one or more of the things named
	 * <tt>PANEL_NAME</tt>. One of these can be either in a tab
	 * or in it's own window.
	 */
	public static final String DISPLAY_NAME = "Display";
	
	public static final String DATASELECTOR_NAME = "Data Selector";
	
	
}
