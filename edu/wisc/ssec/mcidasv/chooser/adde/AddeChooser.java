/*
 * $Id$
 *
 * Copyright  1997-2004 Unidata Program Center/University Corporation for
 * Atmospheric Research, P.O. Box 3000, Boulder, CO 80307,
 * support@unidata.ucar.edu.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package edu.wisc.ssec.mcidasv.chooser.adde;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.w3c.dom.Element;

import ucar.unidata.idv.chooser.IdvChooser;
import ucar.unidata.idv.chooser.IdvChooserManager;
import ucar.unidata.idv.chooser.TimesChooser;
import ucar.unidata.idv.chooser.adde.AddeServer;
import ucar.unidata.idv.chooser.adde.AddeServer.Group;
import ucar.unidata.util.GuiUtils;
import ucar.unidata.util.LogUtil;
import ucar.unidata.util.Misc;
import ucar.unidata.util.StringUtil;
import ucar.unidata.xml.XmlObjectStore;
import edu.wisc.ssec.mcidas.adde.AddeTextReader;
import edu.wisc.ssec.mcidas.adde.AddeURLException;
import edu.wisc.ssec.mcidasv.Constants;
import edu.wisc.ssec.mcidasv.McIDASV;
import edu.wisc.ssec.mcidasv.McIdasPreferenceManager;
import edu.wisc.ssec.mcidasv.ServerPreferenceManager;
import edu.wisc.ssec.mcidasv.ServerPreferenceManager.DatasetDescriptor;
import edu.wisc.ssec.mcidasv.ServerPreferenceManager.ServerPropertyDialog;
import edu.wisc.ssec.mcidasv.ServerPreferenceManager.ServerPropertyDialog.Types;
import edu.wisc.ssec.mcidasv.addemanager.AddeManager;
import edu.wisc.ssec.mcidasv.util.CollectionHelpers;

/**
 *
 * @author Unidata IDV Development Team
 * @version $Revision$
 */
public class AddeChooser extends TimesChooser {

    /** server state preference. Holds the last server/group used */
    private static final String PREF_SERVERSTATE =
        "idv.chooser.adde.serverstate";

    /** Do we remove or mark as inactive the AddeServers and Groups */
    private static final boolean MARK_AS_INACTIVE = true;

    /** My servers */
    private List<AddeServer> addeServers = CollectionHelpers.arrList();

    /** flag for relative times range */
    private static final int TIMES_RELATIVERANGE = 0;

    /** flag for absolute times */
    private static final int TIMES_ABSOLUTE = 1;

    /** flag for relative times */
    private static final int TIMES_RELATIVE = 2;

    /** Command for connecting */
    protected static final String CMD_CONNECT = "cmd.connect";

    /** Property for the PUBLIC.SRV file */
    protected static final String FILE_PUBLICSRV = "PUBLIC.SRV";

    /** ADDE request string for pointdata */
    protected static final String REQ_POINTDATA = "pointdata";

    /** ADDE request string for an image directory */
    protected static final String REQ_IMAGEDIR = "imagedir";

    /** ADDE request string for text */
    protected static final String REQ_TEXT = "text";

    /** ADDE request string for dataset information */
    protected static final String REQ_DATASETINFO = "datasetinfo";

    /** ADDE request string for image data */
    protected static final String REQ_IMAGEDATA = "imagedata";

    /** Default value for the compress property */
    protected static final String DEFAULT_COMPRESS = "gzip";

    /** Default value for the port property */
    protected static final String DEFAULT_PORT = "112";

    /** Default value for the debug property */
    protected static final String DEFAULT_DEBUG = "false";

    /** Default value for the version property */
    protected static final String DEFAULT_VERSION = "1";

    /** Default value for the user property */
    protected static final String DEFAULT_USER = "idv";

    /** Default value for the proj property */
    protected static final String DEFAULT_PROJ = "0";

    /** Property for accessing a file */
    protected static final String PROP_FILE = "file";

    /** Property for image compress */
    protected static final String PROP_COMPRESS = "compress";

    /** Property for image default value descriptor */
    protected static final String PROP_DEBUG = "debug";

    /** Property for image default value descriptor */
    protected static final String PROP_DESCR = "descr";

    /** Property for group */
    protected static final String PROP_GROUP = "group";

    /** Property for num param */
    protected static final String PROP_NUM = "num";

    /** Property for image default value param */
    protected static final String PROP_PARAM = "param";

    /** Property for the port */
    protected static final String PROP_PORT = "port";

    /** Property for the  POS  property */
    protected static final String PROP_POS = "pos";

    /** Property for the  DAY  property */
    protected static final String PROP_DAY = "DAY";

    /** Property for the  TIME  property */
    protected static final String PROP_TIME = "TIME";

    /** Property for the  HMS  property */
    protected static final String PROP_HMS = "HMS";

    /** Property for the  CYD  property */
    protected static final String PROP_CYD = "CYD";

    /** Property for the project */
    protected static final String PROP_PROJ = "proj";

    /** Property for select */
    protected static final String PROP_SELECT = "select";

    /** Property for the user */
    protected static final String PROP_USER = "user";

    /** Property for image default value version */
    protected static final String PROP_VERSION = "version";

    /** Message for selecting times */
    protected static final String MSG_TIMES =
        "Please select one or more times";

    /** Label for data interval */
    protected static final String LABEL_DATAINTERVAL = "Data Interval:";

    /** Label for data set */
    protected static final String LABEL_DATASET = "Dataset:";

    /** Label for data type */
    protected static final String LABEL_DATATYPE = "Data Type:";

    /** Label for server widget */
    protected static final String LABEL_SERVER = "Server:";

    /** Label for stations widget */
    protected static final String LABEL_STATIONS = "Stations:";

    /** Label for times */
    protected static final String LABEL_TIMES = "Times:";

    /** Property for new data selection */
    public static String NEW_SELECTION = "AddeChooser.NEW_SELECTION";

    /** Not connected */
    protected static final int STATE_UNCONNECTED = 0;

    /** Trying to connet */
    protected static final int STATE_CONNECTING = 1;

    /** Have connected */
    protected static final int STATE_CONNECTED = 2;

    /** flag for OK status */
    public static final int STATUS_OK = 0;

    /** flag for status of needs login */
    public static final int STATUS_NEEDSLOGIN = 1;

    /** flag for status of error */
    public static final int STATUS_ERROR = 2;

    /** flag for ignoring combobox changes */
    protected boolean ignoreStateChangedEvents = false;

    /**
     * Property for the dataset name key.
     * @see #getDataSetName()
     */
    public static String DATASET_NAME_KEY = "name";

    /**
     * Public key for data name.
     * @see #getDataName()
     */
    public final static String DATA_NAME_KEY = "data name";

    /** data name */
    public final static String PROP_DATANAME = "dataname";

    /**
     * Used to synchronize access to widgets
     *   (eg: disabling, setting state, etc).
     */
    protected Object WIDGET_MUTEX = new Object();

    /** hashtable of passwords */
    protected Hashtable passwords = new Hashtable();

    /** What is my state */
    private int state = STATE_UNCONNECTED;

    /** UI for selecting a server */
    private JComboBox serverSelector;

    /**
     * This gets updated every time the global list of addeservers is changed. It allows us
     *   to know when to update all of the combo boxes when they are repainted
     */
    public static int serverTimeStamp = 0;

    /** This represents this chooser's current version of the adde servers */
    private int myServerTimeStamp = serverTimeStamp;

    /** Widget for selecting the data group */
    protected JComboBox groupSelector;

    /**
     * List of Component-s that rely on being connected to a server.
     * We have this here so we can enable/disable them
     */
    private List compsThatNeedServer = new ArrayList();

    /** Separator string */
    protected static String separator = "----------------";

    /** Reference back to the server manager */
    protected ServerPreferenceManager serverManager;

    public static JToggleButton mineBtn = null;

    public boolean allServersFlag;

    /** Command for opening up the server manager */
    protected static final String CMD_MANAGER = "cmd.manager";

    private String lastBadServer = "";
    private String lastBadGroup = "";

    private String lastServerName = "";
    private String lastServerGroup = "";
    private String lastServerUser = "";
    private String lastServerProj = "";
    private AddeServer lastServer = new AddeServer("");

    /**
     * Create an AddeChooser associated with an IdvChooser
     *
     *
     * @param mgr The chooser manager
     * @param root The chooser.xml node
     */
    public AddeChooser(IdvChooserManager mgr, Element root) {

        super(mgr, root);
        simpleMode = !getProperty(IdvChooser.ATTR_SHOWDETAILS, true);

        serverSelector = new JComboBox(new Vector(addeServers)) {
            public void paint(Graphics g) {
                if (myServerTimeStamp != serverTimeStamp) {
                    myServerTimeStamp = serverTimeStamp;
                    Misc.runInABit(10, AddeChooser.this, "updateServerList",
                                   null);
                }
                super.paint(g);
            }
        };
        serverSelector.setEditable(true);
        serverSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
//                if (!ignoreStateChangedEvents) {
//                    System.err.println("ignoreStateChangeEvents");
//                    setGroups();
//                }
            }
        });

        serverSelector.getEditor().getEditorComponent().addMouseListener(
            new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                AddeServer server = getAddeServer("server selector mouser");
                if (server == null) {
                    return;
                }
                List items = new ArrayList();
                if (MARK_AS_INACTIVE || server.getIsLocal()) {
                    items.add(GuiUtils.makeMenuItem("Remove local server: "
                            + server.getName(), AddeChooser.this,
                                "removeServer", server));
                } else {
                    items.add(new JMenuItem("Not a local server"));
                }
                JPopupMenu popup = GuiUtils.makePopupMenu(items);
                popup.show(serverSelector, e.getX(), e.getY());
            }
        });

        loadServerState();

        serverSelector = getServerSelector();

        groupSelector = new JComboBox();
        groupSelector.setToolTipText("Right click to remove group");
        groupSelector.setEditable(true);
        groupSelector.getEditor().getEditorComponent().addMouseListener(
            new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Object selected = groupSelector.getSelectedItem();
                    if ((selected == null)
                            || !(selected instanceof AddeServer.Group)) {
                        return;
                    }
                    AddeServer.Group group = (AddeServer.Group) selected;
                    List items = new ArrayList();
                    if (MARK_AS_INACTIVE || group.getIsLocal()) {
                        items.add(
                            GuiUtils.makeMenuItem(
                                "Remove local group: " + group.getName(),
                                AddeChooser.this, "removeGroup", group));
                    }

                    final AddeServer server = getAddeServer("groupSelector mouser");

                    if (server != null) {
                        List groups =
                            server.getGroupsWithType(getGroupType(), false);
                        for (int i = 0; i < groups.size(); i++) {
                            final AddeServer.Group inactiveGroup =
                                (AddeServer.Group) groups.get(i);
                            if (inactiveGroup.getActive()) {
                                continue;
                            }
                            JMenuItem mi =
                                new JMenuItem("Re-activate group: "
                                    + inactiveGroup);
                            items.add(mi);
                            mi.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    getIdv().getIdvChooserManager()
                                        .activateAddeServerGroup(server,
                                            inactiveGroup);
                                    setGroups();
                                    groupSelector.setSelectedItem(
                                        inactiveGroup);

                                }
                            });
                        }
                    }

                    if (items.size() == 0) {
                        items.add(new JMenuItem("Not a local group"));
                    }

                    JPopupMenu popup = GuiUtils.makePopupMenu(items);
                    popup.show(groupSelector, e.getX(), e.getY());
                }
            }
        });

        descriptorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if ( !ignoreDescriptorChange
                        && (e.getStateChange() == e.SELECTED)) {
                    descriptorChanged();
                }
            }
        });

        serverManager = ((McIDASV)getIdv()).getServerManager();
        serverManager.addManagedChooser(this);

        setGroups();

        updateServerList();
    }

    public void updateServers() {
        if (serverManager == null)
            serverManager = ((McIDASV)getIdv()).getServerManager();
        String type = getGroupType();
        List<AddeServer> managedServers = serverManager.getAddeServers(type);

        List<AddeServer> localList = CollectionHelpers.arrList();
        List<AddeServer> remoteList = CollectionHelpers.arrList();
        List<AddeServer> servers = CollectionHelpers.arrList();
        for (AddeServer server : managedServers) {
            if (server.getIsLocal())
                localList.add(server);
            else
                remoteList.add(server);
        }

        // server list doesn't need a separator if there's only remote servers
        if (!localList.isEmpty()) {
            servers.addAll(localList);
            servers.add(new AddeServer(separator));
        }
        servers.addAll(remoteList);
        if (!servers.isEmpty()) {
            addeServers = servers;
            GuiUtils.setListData(serverSelector, addeServers);
            serverSelector.setSelectedIndex(0);
        }
    }

    /**
     * Reload the list of servers if they have changed
     */
    public void updateServerList() {
        updateServers();
        updateGroups();
    }

    public void updateGroups() {
        if (groupSelector == null)
            return;

        List<Group> groups = CollectionHelpers.arrList();
        if (serverSelector.getItemCount() >= 1) {
            Object selected = serverSelector.getSelectedItem();
            if (selected instanceof AddeServer) {
                AddeServer selectedServer = (AddeServer)serverSelector.getSelectedItem();
                if (isServerLocal(selectedServer))
                    groups.addAll(((McIDASV)getIdv()).getAddeManager().getGroups());
                else
                    groups.addAll(((McIDASV)getIdv()).getServerManager().getGroups(selectedServer, getGroupType()));
            }
        }
        GuiUtils.setListData(groupSelector, groups);
    }

    public void updateGroupsOld() {
        if (groupSelector != null) {
            try {
                if (serverSelector.getItemCount() < 1) {
                    groupSelector.removeAllItems();
                } else {
                    List groups = null;
                    AddeServer selectedServer = (AddeServer)serverSelector.getSelectedItem();
                    if (selectedServer != null) {
                        if (isServerLocal(selectedServer)) {
                            McIDASV idv = (McIDASV)getIdv();
                            AddeManager addeManager = idv.getAddeManager();
                            groups = addeManager.getGroups();
                        }
                        else {
                            groups = selectedServer.getGroupsWithType(getGroupType(), true);
                        }
                        if (groups != null) {
                            GuiUtils.setListData(groupSelector, groups);
                            if (groups.size() > 0) groupSelector.setSelectedIndex(0);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Decide if the server you're asking about is actually a separator
     */
    protected static boolean isSeparator(AddeServer checkServer) {
        if (checkServer != null) {
            if (checkServer.getName().equals(separator)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Decide if the server you're asking about is local
     */
    protected static boolean isServerLocal(AddeServer checkServer) {
        if (checkServer != null) {
            String serverName = checkServer.getName();
            if (serverName.length() >= 9 && serverName.startsWith("localhost")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * get the adde server grup type to use
     *
     * @return group type
     */
    protected String getGroupType() {
        return AddeServer.TYPE_ANY;
    }

    /**
     * Remove the group from the global list
     *
     * @param group the group
     */
    public void removeGroup(AddeServer.Group group) {
        AddeServer server = getAddeServer("removeGroup");
        if (server == null) {
            return;
        }
        if ( !MARK_AS_INACTIVE && !group.getIsLocal()) {
            return;
        }
        getIdv().getIdvChooserManager().removeAddeServerGroup(server, group,
                MARK_AS_INACTIVE);
        setGroups();
    }

    /**
     * Remove the server
     *
     * @param server server to remove
     */
    public void removeServer(AddeServer server) {
        if ( !MARK_AS_INACTIVE && !server.getIsLocal()) {
            return;
        }
        getIdv().getIdvChooserManager().removeAddeServer(server,
                MARK_AS_INACTIVE);
        updateServerList();
    }

    private void setBadServer(String name, String group) {
        if (name == null)
            name = "";
        if (group == null)
            group = "";

        lastBadServer = name;
        lastBadGroup = group;
    }

    private boolean isBadServer(String name, String group) {
        assert lastBadServer != null;
        assert lastBadGroup != null;
        return lastBadServer.equals(name) && lastBadGroup.equals(group);
    }

    private void setLastServer(String name, String group, AddeServer server) {
        if (name == null)
            name = "";
        if (group == null)
            group = "";
        if (server == null) {
            server = new AddeServer(name);
            Group addeGroup = new Group(getDataType(), group, group);
            server.addGroup(addeGroup);
        }
        lastServerName = name;
        lastServerGroup = group;
        lastServer = server;
    }
    
    private boolean isLastServer(String name, String group) {
        assert lastServer != null;
        assert lastServerName != null;
        assert lastServerGroup != null;
        return lastServerName.equals(name) && lastServerGroup.equals(group);
    }
    
    /**
     * Get the selected AddeServer
     *
     * @return the server or null
     */
    protected AddeServer getAddeServer(String src) {
//        System.err.println("getAddeServer: from " + src + " thread=" + Thread.currentThread().getName());

        if (lastServerName != null && lastServerName.equals("unset")) {
//            System.err.println("* getAddeServer: returning null because we're still waiting on the dialog");
            return null;
        }

        Object selected = serverSelector.getSelectedItem();
        if ((selected != null) && (selected instanceof AddeServer)) {
            AddeServer server = (AddeServer)selected;

            Map<String, String> accounting = serverManager.getAccounting(server);
            lastServerUser = accounting.get("user");
            lastServerProj = accounting.get("proj");
            setLastServer(server.getName(), getGroup(true), server);
            
//            System.err.println("* getAddeServer: returning AddeServer=" + server.getName() + " group=" + server.getGroups()+" user="+lastServerUser+" proj="+lastServerProj + " ugh: " + accounting.get("user") + " " + accounting.get("proj"));
            return (AddeServer) selected;
        } else if ((selected != null) && (selected instanceof String)) {
            String name = (String)selected;
            String group = getGroup(true);
            if (isBadServer(name, group)) {
//                System.err.println("* getAddeServer: returning null due to text entries being known bad values: name=" + name + " group=" + group);
                return null;
            }
            if (isLastServer(name, group)) {
//                System.err.println("* getAddeServer: returning last server: name=" + lastServer.getName() + " group=" + lastServer.getGroups());
                return lastServer;
            }
            lastServerName = "unset";
            lastServerGroup = "unset";
            ServerPreferenceManager serverManager = ((McIdasPreferenceManager)getIdv().getPreferenceManager()).getServerManager();
            ServerPropertyDialog dialog = new ServerPropertyDialog(null, true, serverManager);
            Set<Types> defaultTypes = EnumSet.of(ServerPropertyDialog.convertDataType(getDataType()));
            dialog.setTitle("Add New Server");
            dialog.showDialog(name, group, defaultTypes);
            boolean hitApply = dialog.hitApply(true);
            if (!hitApply) {
//                System.err.println("* getAddeServer: returning null due to cancel request from showDialog");
                setBadServer(name, group);
                return null;
            }

            Set<DatasetDescriptor> added = dialog.getAddedDatasetDescriptors();
            if (added == null) {
//                System.err.println("* getAddeServer: null list of added servers somehow!");
                setBadServer(name, getGroup(true));
                return null;
            }
            for (DatasetDescriptor descriptor : added) {
                updateServerList();
                AddeServer addedServer = descriptor.getServer();
                serverSelector.setSelectedItem(addedServer);
//                System.err.println("* getAddeServer: returning newly added AddeServer=" + addedServer.getName() + " group=" + addedServer.getGroups());
                setLastServer(name, group, addedServer);
                lastServerUser = descriptor.getUser();
                lastServerProj = descriptor.getProj();
                return addedServer;
            }
        } else if (selected == null) {
//            System.err.println("* getAddeServer: returning null due to null object in selector");
        } else {
//            System.err.println("* getAddeServer: returning null due to unknown object type in selector: " + selected.toString());
        }
        return null;
    }

    /**
     * Set the group list
     */
    protected void setGroups() {
        AddeServer server = getAddeServer("setGroups");
        if (server != null) {
            Object selected = groupSelector.getSelectedItem();
            List   groups   = server.getGroupsWithType(getGroupType());
            GuiUtils.setListData(groupSelector, groups);
            if ((selected != null) && groups.contains(selected)) {
                groupSelector.setSelectedItem(selected);
            }
        } else {
            GuiUtils.setListData(groupSelector, new Vector());
        }
    }

    /**
     * Add a listener to the given combobox that will set the
     * state to unconnected
     *
     * @param box The box to listen to.
     */
    protected void clearOnChange(final JComboBox box) {
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if ( !ignoreStateChangedEvents) {
                    setState(STATE_UNCONNECTED);
                }
            }
        });
    }


    /**
     * Handle when the user presses the connect button
     *
     * @throws Exception On badness
     */
    public void handleConnect() throws Exception {
        AddeServer server = getAddeServer("handleConnect");
        if (server == null) {
            return;
        }
        setState(STATE_CONNECTING);
        handleUpdate();
    }

    /**
     * Handle when the user presses the update button
     *
     * @throws Exception On badness
     */
    public void handleUpdate() throws Exception {}


    /**
     * Handle when the user presses the connect button.
     */
    public void handleConnectFromThread() {
        showWaitCursor();
        try {
            handleConnect();
        } catch (Exception exc) {
//            if (exc != null)
//                exc.printStackTrace();
            handleConnectionError(exc);
        }
        showNormalCursor();
    }

    /**
     * Handle when the user presses the update button
     */
    public void handleUpdateFromThread() {
        showWaitCursor();
        try {
            handleUpdate();
        } catch (Exception exc) {
            System.err.println("handleUpdate generated an exception: " + exc);
            handleConnectionError(exc);
        }
        showNormalCursor();
    }

    /**
     * Connect to the server. Call handleConnect in a thread
     */
    protected final void doConnect() {
        Misc.run(this, "handleConnectFromThread");
    }

    /**
     * Update the selector. Call handleUpdate in a thread
     */
    public final void doUpdate() {
        Misc.run(this, "handleUpdateFromThread");
    }

    /**
     * Handle the event
     *
     * @param ae The event
     */
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if (cmd.equals(CMD_CONNECT)) {
            doConnect();
        } else if (cmd.equals(CMD_MANAGER)) {
            doManager();
        }
        else {
            super.actionPerformed(ae);
        }
    }

    /**
     * Go directly to the Server Manager
     */
    protected void doManager() {
        Object selected = serverSelector.getSelectedItem();
        if (selected instanceof AddeServer) {
            AddeServer selectedServer = (AddeServer)selected;
            if (selectedServer != null) {
                if (isServerLocal(selectedServer)) {
                    ((McIDASV)getIdv()).showAddeManager();
                    return;
                }
            }
        }
        getIdv().getPreferenceManager().showTab(Constants.PREF_LIST_ADDE_SERVERS);
    }

    private static final AddeServer BAD_SERVER = new AddeServer("BAD BAD BAD BAD");

    private AddeServer findLocalServer(final List<AddeServer> servers) {
        assert servers != null;
        for (AddeServer server : servers)
            if (server.getIsLocal() && server.getName().toLowerCase().startsWith("localhost"))
                return server;
        return BAD_SERVER;
    }

    private AddeServer findAddeServer(final List<AddeServer> servers, final String name) {
        assert servers != null;
        assert name != null;

        if (name.toLowerCase().startsWith("localhost"))
            return findLocalServer(servers);

        for (AddeServer server : servers)
            if (!server.getIsLocal() && Misc.equals(server.getName(), name))
                return server;
        return BAD_SERVER;
    }

    /**
     * Load any saved server state
     */
    protected void loadServerState() {
        if (addeServers == null) {
            return;
        }
        String id = getId();
        String[] serverState =
            (String[]) getIdv().getStore().get(PREF_SERVERSTATE + "." + id);
        if (serverState == null) {
            return;
        }

        AddeServer server = findAddeServer(addeServers, serverState[0]);
        if (server == null || server == BAD_SERVER)
            return;

        serverSelector.setSelectedItem(server);
        setGroups();

        // not exactly fond of this approach, but ucar.unidata.util.NamedThing
        // has a toString that gives priority to the description rather than
        // the name. Servers + groups are updated from the server manager anyway.
        for (Group group : (List<Group>)server.getGroups())
            group.setDescription(null);

        if (serverState[1] != null) {
            Group group = (Group)server.findGroup(serverState[1]);
            if (group != null)
                groupSelector.setSelectedItem(group.getName());
        }
    }

    /**
     * Save the server state
     */
    public void saveServerState() {
        String   id          = getId();
        String server = getAddeServer("saveServerState").getName();
        String[] serverState = { server, getGroup() };
        getIdv().getStore().put(PREF_SERVERSTATE + "." + id, serverState);
        getIdv().getStore().save();
    }

    /**
     * Enable or disable the components in the compsThatNeedServer list
     */
    private void enableComps() {
        synchronized (WIDGET_MUTEX) {
            boolean newEnabledState = (state == STATE_CONNECTED);
            for (int i = 0; i < compsThatNeedServer.size(); i++) {
                JComponent comp = (JComponent) compsThatNeedServer.get(i);
                if (comp.isEnabled() != newEnabledState) {
                    GuiUtils.enableTree(comp, newEnabledState);
                }
            }
        }
    }

    protected List processServerComponents() {
        if (groupSelector != null)
            clearOnChange(groupSelector);

        descriptorLabel = addServerComp(GuiUtils.rLabel(getDescriptorLabel()+":"));
        descriptorComboBox = new JComboBox();

        descriptorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (!ignoreDescriptorChange && (e.getStateChange() == e.SELECTED)) {
                    descriptorChanged();
                }
            }
        });

        JButton showBtn =
            GuiUtils.makeImageButton("/auxdata/ui/icons/About16.gif", this,
                                     "showGroups", null, true);
        showBtn.setToolTipText(
            "List the public datasets available on the server");

//        JComponent extraTop = GuiUtils.hbox(groupSelector, showBtn);
        JComponent extraTop = GuiUtils.hbox(groupSelector, showBtn);
        List<JComponent> comps = new ArrayList<JComponent>();
        addTopComponents(comps, LABEL_DATASET, extraTop);
        return comps;
    }

    /**
     * Add to the given comps list all the status line and server
     * components.
     *
     * @param comps List of comps to add to
     * @param label The label to add after the server selector
     * @param extra The component to add after the label (usually a combobox)
     */
    protected void addTopComponents(List<JComponent> comps, String label,
                                    JComponent extra) {
        addTopComponents(comps, GuiUtils.hbox(new JLabel(label), extra, GRID_SPACING));
    }

    /**
     * Add to the given comps list all the status line and server
     * components.
     *
     * @param comps List of comps to add to
     * @param extra The components after the server box if non-null.
     */
    protected void addTopComponents(List<JComponent> comps, Component extra) {
        comps.add(GuiUtils.rLabel(""));
        comps.add(getStatusComponent());
        comps.add(GuiUtils.rLabel(LABEL_SERVER));
        if (extra == null)
            extra = GuiUtils.filler();

        GuiUtils.tmpInsets = GRID_INSETS;
        mineBtn =
            GuiUtils.getToggleImageButton("/edu/wisc/ssec/mcidasv/resources/icons/toolbar/internet-web-browser16.png",
                                     "/edu/wisc/ssec/mcidasv/resources/icons/toolbar/system-software-update16.png",
                                     0, 0, true);
        mineBtn.setContentAreaFilled(false);
        mineBtn.setSelected(allServersFlag);
        mineBtn.setToolTipText("Toggle system servers on/off after mine");
        mineBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showServers();
            }
        });

        JComponent mine = GuiUtils.hbox(Misc.newList(serverSelector));
        JPanel right = GuiUtils.doLayout(new Component[] { mine, extra, 
            getConnectButton(), getManageButton() },4, GuiUtils.WT_YN,GuiUtils.WT_N);
        comps.add(GuiUtils.left(right));
    }

    public void showServers() {
        allServersFlag = !allServersFlag;
        XmlObjectStore store = getIdv().getStore();
        store.put(Constants.PREF_SYSTEMSERVERSIMG, allServersFlag);
        store.save();
        updateServers();
        updateGroups();
    }

    /**
     * Create the 'Manage...' button.
     *
     * @return The manage button.
     */
    protected JComponent getManageButton() {
        JButton managerBtn = new JButton("Manage...");
        managerBtn.setActionCommand(CMD_MANAGER);
        managerBtn.addActionListener(this);
        return registerStatusComp("manager", managerBtn);
        //         return managerBtn;
    }

    private String stateToString(final int state) {
        switch (state) {
            case STATE_CONNECTED: return "connected to server";
            case STATE_UNCONNECTED: return "not connected";
            case STATE_CONNECTING: return "connecting to server";
            default: return "unknown state: " + getState();
        }
    }

    /**
     * Disable/enable any components that depend on the server.
     * Try to update the status labelwith what we know here.
     */
    protected void updateStatus() {
//        System.err.println("updateStatus: incoming state: " + stateToString(state));
        super.updateStatus();

        //Put this in a thread to fix the enabled but shown 
        //as disabled bug
        Misc.run(new Runnable() {
            public void run() {
                enableComps();
            }
        });

        if (state != STATE_CONNECTED) {
            clearTimesList();
        }
        if (state == STATE_UNCONNECTED) {
            setStatus("Please connect to the server", "connect");
        } else if (state == STATE_CONNECTING) {
            AddeServer server = getAddeServer("updateStatus");
            if (server != null)
                setStatus("Connecting to server: " + server.getName());
//            else 
//                System.err.println("updateStatus: handled cancel");
        } else if (getGoodToGo()) {
            setStatus("Press \"" + CMD_LOAD + "\" to load the selected "
                      + getDataName().toLowerCase(), "buttons");

        } else if (getState() == STATE_CONNECTED) {
            if (usingStations() && (stationMap.getStations().size() > 0)
                    && (getSelectedStations().size() == 0)) {
                if (stationMap.getMultipleSelect()) {
                    setStatus("Please select one or more stations",
                              "stations");
                } else {
                    setStatus("Please select a station", "stations");
                }
                if (stationMap.getDeclutter()) {
                    //                    setStatus(
                    //                        getStatusLabel().getText(), "stations");
                }
            } else if ( !haveTimeSelected()) {
                setStatus(MSG_TIMES);
            }
            
//            System.err.println("updateStatus: connect good; clearing out lastServer stuff");
            lastServer = new AddeServer("");
            lastServerGroup = "";
            lastServerName = "";
            lastServerProj = "";
            lastServerUser = "";
        }
        setHaveData(getGoodToGo());
    }


    /**
     * This allows derived classes to provide their own name for labeling, etc.
     *
     * @return  the dataset name
     */
    public String getDataName() {
        return "ADDE data";
    }

    /**
     * This allows derived classes to provide their own name for the dataset.
     *
     * @return  the dataset name
     */
    public String getDataSetName() {
        return "ADDE data";
    }

    /**
     * Get the data type ID
     *
     * @return  the data type
     */
    public String getDataType() {
        return "ANY";
    }


    /**
     * Get the tooltip for the load button
     *
     * @return The tooltip for the load button
     */
    protected String getLoadToolTip() {
        return "Load the selected " + getDataName().toLowerCase();
    }

    /**
     * Read the adde text url and return the lines of text.
     * If unsuccessful return null.
     *
     * @param url adde url to a text file
     *
     * @return List of lines or null if in error
     */
    protected List readTextLines(String url) {
        AddeTextReader reader = new AddeTextReader(url);
        if ( !reader.getStatus().equals("OK")) {
            return null;
        }
        return reader.getLinesOfText();
    }


    /**
     * Read the groups from the public.srv file on the server
     *
     * @return List of groups
     */
    protected List readGroups() {
        List groups = new ArrayList();
        try {
            String dataType = getDataType();
            String type = ((dataType.length() > 0)
                              ? "TYPE=" + dataType
                              : "TYPE=NOTYPE");
            StringBuffer buff = getUrl(REQ_TEXT);
            System.err.println("readGroups: url=" + buff);
            appendKeyValue(buff, PROP_FILE, FILE_PUBLICSRV);
            List lines = readTextLines(buff.toString());
            //            System.err.println ("lines:" + StringUtil.join("\n",lines));
            if (lines == null) {
                return null;
            }
            Hashtable seen = new Hashtable();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).toString();
                if (line.indexOf(type) < 0) {
                    continue;
                }
                List toks = StringUtil.split(line, ",", true, true);
                if (toks.size() == 0) {
                    continue;
                }
                String tok = (String) toks.get(0);
                int    idx = tok.indexOf("=");
                if (idx < 0) {
                    continue;
                }
                if ( !tok.substring(0, idx).trim().equals("N1")) {
                    continue;
                }
                String group = tok.substring(idx + 1).trim();
                if (seen.get(group) != null) {
                    continue;
                }
                seen.put(group, group);
                groups.add(group);
            }
        } catch (Exception e) {
            return null;
        }
        return groups;
    }

    /**
     * Handle unknown data set error
     */
    protected void handleUnknownDataSetError() {
        //Don't do this for now
        //        List groups =  readGroups();
        List groups = null;
        if (groups == null) {
            LogUtil.userErrorMessage("Dataset not found on server: "
                                     + getAddeServer("handleUnknownDatasetErr 1").getName());

        } else {
            LogUtil.userErrorMessage("Dataset not found on server: "
                                     + getAddeServer("handleUnknownDatasetErr 2").getName()
                                     + "\nPossible data sets:\n" + "   "
                                     + StringUtil.join("\n   ", groups));

        }
        setState(STATE_UNCONNECTED);
    }

    /**
     * Show the given error to the user. If it was an Adde exception
     * that was a bad server error then print out a nice message.
     *
     * @param excp The exception
     */
    protected void handleConnectionError(Exception excp) {
        String message = excp.getMessage();
        if (excp instanceof AddeURLException) {
            System.err.println("handle connection error: " + excp);
            handleUnknownDataSetError();

        } else if (message.toLowerCase().indexOf("unknownhostexception")
                   >= 0) {
            LogUtil.userErrorMessage("Could not access server: "
                                     + getAddeServer("handleConnectionErr 1").getName());
        } else if (message.toLowerCase().indexOf(
                "server unable to resolve this dataset") >= 0) {
            System.err.println("handle connection error: resolving: " + message);
            handleUnknownDataSetError();
        } else if ((message.toLowerCase().indexOf("no images satisfy") >= 0)
                   || (message.toLowerCase().indexOf(
                       "error generating list of files") >= 0)) {
            LogUtil.userErrorMessage("No data available for the selection");
            return;
        } else {
            LogUtil.logException("Error connecting to: " + getAddeServer("handleConnectionErr 2").getName(), excp);
        }
        if ( !(getState() == STATE_CONNECTED)) {
            setState(STATE_UNCONNECTED);
        }
    }


    /**
     * Set the current state. This also triggers a status update
     *
     * @param newState The new state
     */
    protected void setState(int newState) {
        this.state = newState;
        updateStatus();
    }

    /**
     * Get the state
     *
     * @return The state
     */
    protected int getState() {
        return state;
    }




    /**
     * Add the given component to the list of components that depend on a connection.
     *
     * @param comp The component
     *
     * @return The same component
     */
    protected JComponent addServerComp(JComponent comp) {
        compsThatNeedServer.add(comp);
        return comp;
    }

    /**
     * Create the 'Connect' button.
     *
     * @return The connect button.
     */
    protected JComponent getConnectButton() {
        JButton connectBtn = new JButton("Connect");
        connectBtn.setActionCommand(CMD_CONNECT);
        connectBtn.addActionListener(this);
        return registerStatusComp("connect", connectBtn);
        //        return connectBtn;
    }

    /**
     *  Do what needs to be done to read in the times.  Subclasses
     *  need to implement this.
     */
    protected void readTimes() {}

    /**
     * Are we all set to load data.
     *
     * @return All set to load.
     */
    protected boolean getGoodToGo() {
        if (state != STATE_CONNECTED) {
            return false;
        }
        if ((stationMap != null) && !haveStationSelected()) {
            return false;
        }
        if ( !haveTimeSelected()) {
            return false;
        }
        return true;
    }

    /**
     * A utility method to make a name=value part of the adde request string
     *
     * @param buf The buffer to append to
     * @param name The property name
     * @param value The value
     */
    protected void appendKeyValue(StringBuffer buf, String name,
                                  String value) {
        if ((buf.length() == 0) || (buf.charAt(buf.length() - 1) != '?')) {
            buf.append("&");
        }
        buf.append(name);
        buf.append("=");
        buf.append(value);
    }

    private String convertStatusToString(final int status) {
        switch (status) {
            case STATUS_OK: return "everything is a-ok";
            case STATUS_NEEDSLOGIN: return "need accounting info";
            case STATUS_ERROR: return "some kind of error";
            default: return "unknown status";
        }
    }

    /**
     * Check if the server is ok
     *
     * @return status code
     */
    protected int checkIfServerIsOk() {
        try {
            StringBuffer buff = getUrl(REQ_TEXT);
            appendKeyValue(buff, PROP_FILE, FILE_PUBLICSRV);
//            System.err.println("checkIfServerOk: " + buff);
            URL           url  = new URL(buff.toString());
            URLConnection urlc = url.openConnection();
            InputStream   is   = urlc.getInputStream();
            is.close();
            return STATUS_OK;
        } catch (AddeURLException ae) {
            String aes = ae.toString();
            if (aes.indexOf("Invalid project number") >= 0) {
                LogUtil.userErrorMessage("Invalid project number");
                return STATUS_NEEDSLOGIN;
            }
            if (aes.indexOf("Invalid user id") >= 0) {
                LogUtil.userErrorMessage("Invalid user ID");
                return STATUS_NEEDSLOGIN;
            }
            if (aes.indexOf("Accounting data") >= 0) {
                return STATUS_NEEDSLOGIN;
            }
            if (aes.indexOf("cannot run server 'txtgserv") >= 0) {
                return STATUS_OK;
            }
            LogUtil.userErrorMessage("Error connecting to server. "
                                     + ae.getMessage());
            return STATUS_ERROR;
        } catch (Exception exc) {
            AddeServer server = getAddeServer("checkServerOk");
            if (server != null)
                logException("Connecting to server:" + server.getName(), exc);
//            else
//                System.err.println("* checkIfServerIsOk: got a cancel request?");
            return STATUS_ERROR;
        }
    }

    /**
     * This method checks if the current server is valid. If it is valid
     * then it checks if there is authentication required
     *
     * @return true if the server exists and can be accessed
     */
    protected boolean canAccessServer() {
        //Try reading the public.serv file to see if we need a username/proj
        JTextField projFld   = null;
        JTextField userFld   = null;
        JComponent contents  = null;
        JLabel     label     = null;
        boolean    firstTime = true;
        while (true) {
            int status = checkIfServerIsOk();
            if (status == STATUS_OK) {
//                System.err.println("canAccessServer: connected");
                break;
            }
            if (status == STATUS_ERROR) {
                setState(STATE_UNCONNECTED);
//                System.err.println("canAccessServer: could not connect");
                return false;
            }
            
            AddeServer server = getAddeServer("canAccess");

            if (projFld == null) {
                projFld = new JTextField("", 10);
                userFld = new JTextField("", 10);
                GuiUtils.tmpInsets = GuiUtils.INSETS_5;

                contents = GuiUtils.doLayout(new Component[] {
                  GuiUtils.rLabel("User ID:"), userFld, 
                  GuiUtils.rLabel("Project #:"), projFld, }, 2, GuiUtils.WT_N, 
                  GuiUtils.WT_N);

                label  = new JLabel(" ");
                contents = GuiUtils.topCenter(label, contents);
                contents = GuiUtils.inset(contents, 5);
            }

            String lbl = (firstTime
                ? "The server: " + server.getName()
                    + " requires a user ID & project number for access"
                    : "Authentication for server: " + server.getName()
                    + " failed. Please try again");
            label.setText(lbl);

            if ( !GuiUtils.showOkCancelDialog(null, "ADDE Project/User name",
                contents, null)) {
                setState(STATE_UNCONNECTED);
                System.err.println("canAccessServer: cancel dialog?");
                return false;
            }
            firstTime = false;
            String userName = userFld.getText().trim();
            String project = projFld.getText().trim();
            if ((userName.length() > 0) && (project.length() > 0)) {
                passwords.put(server.getName(),
                    new String[] { userName, project });
            }
//            }
        }
//        System.err.println("canAccessServer: returning true");
        return true;
    }

    /**
     * Create the first part of the ADDE request URL
     *
     * @param requestType     type of request
     * @return  ADDE URL prefix
     */
    protected StringBuffer getUrl(String requestType) {
        StringBuffer buff = new StringBuffer("adde://" + getAddeServer("getUrl").getName() + "/"
                                             + requestType + "?");
        appendMiscKeyValues(buff);
        return buff;
    }

    /**
     * Create the first part of the ADDE request url and append the
     * group argument to it
     *
     * @param requestType    request type
     * @param group          image group
     * @return  ADDE URL prefix
     */
    protected StringBuffer getGroupUrl(String requestType, String group) {
        StringBuffer buff = getUrl(requestType);
        appendKeyValue(buff, PROP_GROUP, group);
        return buff;
    }

    /**
     * Get any extra key=value pairs that are appended to all requests.
     *
     * @param buff The buffer to append onto
     */
    protected void appendMiscKeyValues(StringBuffer buff) {
        appendKeyValue(buff, PROP_COMPRESS, DEFAULT_COMPRESS);
        appendKeyValue(buff, PROP_PORT, DEFAULT_PORT);
        appendKeyValue(buff, PROP_DEBUG, DEFAULT_DEBUG);
        appendKeyValue(buff, PROP_VERSION, DEFAULT_VERSION);
        appendKeyValue(buff, PROP_USER, getLastAddedUser());
        appendKeyValue(buff, PROP_PROJ, getLastAddedProj());
    }

    public String getLastAddedUser() {
        if (lastServerUser != null && lastServerUser.length() > 0) {
//            System.err.println("appendMisc: using dialog user=" + lastServerUser);
            return lastServerUser;
        }
        else {
//            System.err.println("appendMisc: using default user=" + DEFAULT_USER);
            return DEFAULT_USER;
        }
    }

    public String getLastAddedProj() {
       if (lastServerProj != null && lastServerProj.length() > 0) {
//            System.err.println("appendMisc: using dialog proj=" + lastServerProj);
            return lastServerProj;
        }
        else {
//            System.err.println("appendMisc: using default proj=" + DEFAULT_PROJ);
            return DEFAULT_PROJ;
        }
    }

    /**
     * Get the list of properties for the miscellaneous keywords
     * @return list of properties
     */
    protected String[] getMiscKeyProps() {
        return new String[] {
            PROP_COMPRESS, PROP_PORT, PROP_DEBUG, PROP_VERSION, PROP_USER,
            PROP_PROJ
        };
    }

    /**
     * Get the miscellaneous URL keywords
     * @return the key value pairs
     */
    public String getMiscKeywords() {
        StringBuffer buff = new StringBuffer();
        appendMiscKeyValues(buff);
        return buff.toString();
    }

    /**
     * Get the default selected index for the relative times list.
     *
     * @return default index
     */
    protected int getDefaultRelativeTimeIndex() {
        return 0;
    }

    /**
     * Enable or disable the GUI widgets based on what has been
     * selected.
     */
    protected void enableWidgets() {
        super.enableWidgets();
        boolean connected = (getState() == STATE_CONNECTED);
        GuiUtils.enableTree(timesContainer, connected);
        //JDM        absTimesPanel.setEnabled(getDoAbsoluteTimes() && connected);
        //       getRelativeTimesChooser().setEnabled( !getDoAbsoluteTimes()
        //                && connected);
    }

    /** descriptor label */
    protected JComponent descriptorLabel = addServerComp(GuiUtils.rLabel(getDescriptorLabel()+":"));
    
    /** A widget for the list of dataset descriptors */
    protected JComboBox descriptorComboBox = new JComboBox();
    
    /** Flag to keep from infinite looping */
    private boolean ignoreDescriptorChange = false;

    /**
     * Respond to a change in the descriptor list.
     */
    protected void descriptorChanged() {
        readTimes();
        updateStatus();
    }

    /**
     * Get the descriptor widget label. Override this.
     *
     * @return  label for the descriptor  widget
     */
    public String getDescriptorLabel() { return "Any Type"; }
    
//    protected List processServerComponents() {
//        if (groupSelector != null)
//            clearOnChange(groupSelector);
//
//        descriptorLabel = addServerComp(GuiUtils.rLabel(getDescriptorLabel()+":"));
//        descriptorComboBox = new JComboBox();
//
//        descriptorComboBox.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (!ignoreDescriptorChange && (e.getStateChange() == e.SELECTED)) {
//                    descriptorChanged();
//                }
//            }
//        });
//
//        JButton showBtn =
//            GuiUtils.makeImageButton("/auxdata/ui/icons/About16.gif", this,
//                                     "showGroups", null, true);
//        showBtn.setToolTipText(
//            "List the public datasets available on the server");
//
////        JComponent extraTop = GuiUtils.hbox(groupSelector, showBtn);
//        JComponent extraTop = GuiUtils.hbox(groupSelector, showBtn);
//        List comps = new ArrayList();
//        addTopComponents(comps, LABEL_DATASET, extraTop);
//        return comps;
//    }

    /**
     * Show the groups dialog.  This method is not meant to be called
     * but is public by reason of implementation (or insanity).
     */
    public void showGroups() {
        List groups = readGroups();
        if ((groups == null) || (groups.size() == 0)) {
            LogUtil.userMessage("No public datasets found on " + getAddeServer("showGroups 1").getName());
            return;
        }
        final JDialog dialog = GuiUtils.createDialog("Server Groups", true);
        final String[] selected = { null };
        List comps = new ArrayList();
        for (int i = 0; i < groups.size(); i++) {
            final String group = groups.get(i).toString();
            JButton btn = new JButton(group);
            comps.add(btn);
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    selected[0] = group;
                    dialog.dispose();
                }
            });
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });

        JComponent buttons = GuiUtils.vbox(comps);
        buttons = new JScrollPane(GuiUtils.vbox(comps));
        int xsize = ((JComponent) comps.get(0)).getPreferredSize().width;
        buttons.setPreferredSize(new Dimension(xsize + 50, 150));
        JComponent top =
            GuiUtils.inset(new JLabel("Available data sets on server: "
                                      + getAddeServer("showGroups 2").getName()), 5);
        JComponent bottom = GuiUtils.inset(closeBtn, 5);
        JComponent contents = GuiUtils.topCenterBottom(top, buttons,
                                  GuiUtils.wrap(bottom));
        dialog.setLocation(200, 200);
        dialog.getContentPane().add(contents);
        dialog.pack();
        dialog.setVisible(true);
        if (selected[0] != null) {
            groupSelector.setSelectedItem(selected[0]);
            doConnect();
        }
    }

    protected String getGroup() {
        return getGroup(false);
    }

    /**
     * Get the image group from the GUI.
     *
     * @return The image group.
     */
    protected String getGroup(final boolean fromGetServer) {
        Object selected = groupSelector.getSelectedItem();
        if (selected == null) {
            return null;
        }
        if (selected instanceof AddeServer.Group) {
            AddeServer.Group group = (AddeServer.Group) selected;
            return group.getName();
        }

        String groupName = selected.toString().trim();
        if (!fromGetServer && (groupName.length() > 0)) {
            //Force the get in case they typed a server name
            getAddeServer("getGroup 1").getName();
            AddeServer server = getAddeServer("getGroup 2");
            if (server != null) {
                AddeServer.Group group =
                    getIdv().getIdvChooserManager().addAddeServerGroup(
                        server, groupName, getGroupType());
                if (!group.getActive()) {
                    getIdv().getIdvChooserManager().activateAddeServerGroup(
                        server, group);
                }
                //Now put the list of groups back in to the selector
                setGroups();
                groupSelector.setSelectedItem(group);
            }
        }
        return groupName;
    }

    /**
     * Get the server selector
     * @return The server selector
     */
    public JComboBox getServerSelector() {
        if (serverSelector == null)
            serverSelector = new JComboBox();

        ItemListener[] ell = serverSelector.getItemListeners();
        serverSelector.removeItemListener((ItemListener)ell[0]);
        updateServers();
        updateGroups();
        serverSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateGroups();
            }
        });
        serverSelector.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            public void keyTyped(final KeyEvent e) {}
            public void keyPressed(final KeyEvent e) {}
            public void keyReleased(final KeyEvent e) {
                JTextField field = (JTextField)serverSelector.getEditor().getEditorComponent();
                boolean partialMatch = false;
                for (int i = 0; i < serverSelector.getItemCount(); i++) {
                    String entry = serverSelector.getItemAt(i).toString();
                    if (entry.toLowerCase().startsWith(field.getText().toLowerCase()))
                        partialMatch = true;
                }
                
                if (!partialMatch && groupSelector != null) {
                    ((JTextField)groupSelector.getEditor().getEditorComponent()).setText("");
                }
            }
        });
        return serverSelector;
    }
    
//    /**
//     * Get the server selector
//     * @return The server selector
//     */
//    public JComboBox getServerSelector() {
//        return serverSelector;
//    }
//    
//    /**
//     * Connect to the server.
//     */
//    protected void connectToServer() {
//        setDescriptors(null);
//        archiveDay = null;
//        if (archiveDayLabel != null) {
//            archiveDayLabel.setText("");
//        }
//        // set to relative times
//        setDoAbsoluteTimes(false);
//        if ( !canAccessServer()) {
//            return;
//        }
//        readSatBands();
//        readDescriptors();
//        readTimes();
//        //Save the server/group state
//        saveServerState();
//        ignoreStateChangedEvents = true;
//        if (descList != null) {
//            descList.saveState(groupSelector);
//        }
//        ignoreStateChangedEvents = false;
//    }
}

