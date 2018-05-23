package de.fhg.iais.roberta.javaServer.restInterfaceTest;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientGroup;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientUser;
import de.fhg.iais.roberta.main.ServerStarter;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.testutil.JSONUtilForServer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Testing the REST interface for groups of the OpenRoberta server
 *
 * @author Evgeniya
 */
public class ClientGroupTest {

    private SessionFactoryWrapper sessionFactoryWrapper; // used by REST services to retrieve data base sessions
    private DbSetup memoryDbSetup; // use to query the test data base, change the data base at will, etc.

    private Response response; // store all REST responses here
    private HttpSessionState sPid; // reference user 1 is "pid"
    private HttpSessionState sMinscha; // reference user 2 is "minscha"

    // objects for specialized user stories
    private String connectionUrl;

    private RobotCommunicator brickCommunicator;

    private ClientUser restUser;
    private ClientGroup restGroup;

    @Before
    public void setup() throws Exception {
        Properties robertaProperties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(robertaProperties);

        this.connectionUrl = "jdbc:hsqldb:mem:performanceInMemoryDb";
        this.brickCommunicator = new RobotCommunicator();
        this.restUser = new ClientUser(this.brickCommunicator, null);
        this.restGroup = new ClientGroup(this.brickCommunicator);

        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        Session nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
        Map<String, IRobotFactory> robotPlugins = new HashMap<>();
        loadPlugin(robotPlugins);
        this.sPid = HttpSessionState.init(this.brickCommunicator, robotPlugins, 1);
    }

    @Test
    public void createGroupNotNull() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertEquals(8, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS"));
        Assert.assertTrue(this.sPid.isUserLoggedIn());
        long initNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        restGroup(this.sPid, "{'cmd':'createGroup';'userId':'0','groupName':'restTestGroup1716';}", "ok", Key.GROUP_CREATE_SUCCESS);
        long finalNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        long diff = finalNumberOfGroups - initNumberOfGroups;
        Assert.assertEquals(1, diff);
    }

    @Test
    public void createGroupNullWrongSymbol() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertEquals(8, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS"));
        Assert.assertTrue(this.sPid.isUserLoggedIn());
        long initNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        restGroup(this.sPid, "{'cmd':'createGroup';'userId':'0','groupName':'<><><><';}", "error", Key.GROUP_CREATE_ERROR_NOT_SAVED_TO_DB);
        long finalNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        long diff = finalNumberOfGroups - initNumberOfGroups;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void createGroupNull() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertEquals(8, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS"));
        Assert.assertTrue(this.sPid.isUserLoggedIn());
        long initNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        restGroup(this.sPid, "{'cmd':'createGroup';'userId':'0','groupName':'';}", "error", Key.GROUP_CREATE_ERROR_NOT_SAVED_TO_DB);
        long finalNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        long diff = finalNumberOfGroups - initNumberOfGroups;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void deleteGroupNotNull() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertEquals(9, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS"));
        Assert.assertTrue(this.sPid.isUserLoggedIn());
        long initNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        restGroup(this.sPid, "{'cmd':'deleteGroup';'groupName':'restTestGroup1716'}", "ok", Key.GROUP_DELETE_SUCCESS);
        long finalNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        long diff = finalNumberOfGroups - initNumberOfGroups;
        Assert.assertEquals(-1, diff);
    }

    @Test
    public void deleteGroupNull() throws Exception {
        long initNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        restGroup(this.sPid, "{'cmd':'deleteGroup';'groupName':'ghjghj'}", "error", Key.GROUP_DELETE_ERROR);
        long finalNumberOfGroups = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from GROUPS");
        long diff = finalNumberOfGroups - initNumberOfGroups;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void getGroupMembersNotNull() throws Exception {
        restGroup(this.sPid, "{'cmd':'getMembersList';'groupName':'restTestGroup15'}", "ok", Key.GROUP_GET_MEMBERS_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains("bert") && this.response.getEntity().toString().contains("minscha"));
    }

    @Test
    public void getGroupMembersNull() throws Exception {
        restGroup(this.sPid, "{'cmd':'getMembersList';'groupName':'Qererett'}", "error", Key.SERVER_ERROR);
    }

    @Test
    public void getGroupMembersZero() throws Exception {
        restGroup(this.sPid, "{'cmd':'getMembersList';'groupName':'TestGroup2'}", "ok", Key.GROUP_GET_MEMBERS_SUCCESS);
    }

    @Test
    public void getMemberGroups() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restGroup(this.sPid, "{'cmd':'getGroupsList'}", "ok", Key.USER_GET_GROUPS_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains("\"restTestGroup15\",\"robb\""));
    }

    @Test
    public void getGroupNotNull() throws Exception {
        restGroup(this.sPid, "{'cmd':'getGroup';'groupName':'TestGroup1'}", "ok", Key.GROUP_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains("[id=2, owner=1]"));
    }

    @Test
    public void getGroupNull() throws Exception {
        restGroup(this.sPid, "{'cmd':'getGroup';'groupName':'qwqwqw'}", "error", Key.GROUP_GET_ONE_ERROR_NOT_FOUND);
    }

    @Test
    public void getUserGroupNotNull() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restGroup(this.sPid, "{'cmd':'getUserGroup';'account':'bert';'groupName':'restTestGroup1716'}", "ok", Key.USER_GROUP_GET_ONE_SUCCESS);
        Assert.assertTrue(this.response.getEntity().toString().contains("[id=7, userId=3, group=9]"));
    }

    @Test
    public void getUserGroupNull() throws Exception {
        restGroup(this.sPid, "{'cmd':'getUserGroup';'account':'bert';'groupName':'Test576'}", "error", Key.SERVER_ERROR);
    }

    @Test
    public void addUserNotNull() throws Exception {
        restUser(
            this.sPid,
            "{'cmd':'createUser';'accountName':'bert';'userName':'robb';'password':'bert';'userEmail':'cavy@home';'role':'STUDENT'}",
            "ok",
            Key.USER_CREATE_SUCCESS);
        restUser(
            this.sPid,
            "{'cmd':'createUser';'accountName':'minscha';'userName':'cavy';'password':'12';'userEmail':'cavy2@home';'role':'STUDENT'}",
            "ok",
            Key.USER_CREATE_SUCCESS);
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        restGroup(this.sPid, "{'cmd':'createGroup';'userId':'0','groupName':'restTestGroup15';}", "ok", Key.GROUP_CREATE_SUCCESS);
        Assert.assertEquals(5, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'addUser';'account':'minscha';'groupName':'restTestGroup15'}", "ok", Key.USER_GROUP_SAVE_SUCCESS);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(1, diff);
    }

    @Test
    public void addUserNullGroup() throws Exception {
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'addUser';'account':'bert';'groupName':'TestGroup98989'}", "error", Key.SERVER_ERROR);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void addUserNullUser() throws Exception {
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'addUser';'account':'bert78';'groupName':'restTestGroup15'}", "error", Key.USER_TO_ADD_NOT_FOUND);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void deleteUserNotNull() throws Exception {
        restUser(this.sPid, "{'cmd':'login';'accountName':'bert';'password':'bert'}", "ok", Key.USER_GET_ONE_SUCCESS);
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'deleteUser';'account':'bert';'groupName':'restTestGroup15'}", "ok", Key.USER_GROUP_DELETE_SUCCESS);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(-1, diff);
    }

    @Test
    public void deleteUserNullGroup() throws Exception {
        Assert.assertEquals(6, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'deleteUser';'account':'bert';'groupName':'TestGroup551545'}", "error", Key.SERVER_ERROR);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(0, diff);
    }

    @Test
    public void deleteUserNullUser() throws Exception {
        Assert.assertEquals(7, this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP"));
        long initNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        restGroup(this.sPid, "{'cmd':'deleteUser';'account':'bert4545';'groupName':'restTestGroup15'}", "error", Key.USER_GROUP_DELETE_ERROR);
        long finalNumberOfUsersInGroup = this.memoryDbSetup.getOneBigIntegerAsLong("select count(*) from USER_GROUP");
        long diff = finalNumberOfUsersInGroup - initNumberOfUsersInGroup;
        Assert.assertEquals(0, diff);
    }

    private void restGroup(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
        this.response = this.restGroup.command(httpSession, this.sessionFactoryWrapper.getSession(), JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    /**
     * call a REST service for user-related commands. Store the response into <code>this.response</code>. Check whether the expected result and the expected
     * message key are found
     *
     * @param httpSession the session on which behalf the call is executed
     * @param jsonAsString the command (will be parsed to a JSON object)
     * @param result the expected result is either "ok" or "error"
     * @param msgOpt optional key for the message; maybe null
     * @throws Exception
     */
    private void restUser(HttpSessionState httpSession, String jsonAsString, String result, Key msgOpt) throws Exception {
        this.response = this.restUser.command(httpSession, this.sessionFactoryWrapper.getSession(), JSONUtilForServer.mkD(jsonAsString));
        JSONUtilForServer.assertEntityRc(this.response, result, msgOpt);
    }

    private void loadPlugin(Map<String, IRobotFactory> robotPlugins) {
        try {
            @SuppressWarnings("unchecked")
            Class<IRobotFactory> factoryClass =
                (Class<IRobotFactory>) ServerStarter.class.getClassLoader().loadClass("de.fhg.iais.roberta.factory.EV3lejosFactory");
            Constructor<IRobotFactory> factoryConstructor = factoryClass.getDeclaredConstructor(RobotCommunicator.class);
            robotPlugins.put("ev3lejos", factoryConstructor.newInstance(this.brickCommunicator));
        } catch ( Exception e ) {
            throw new DbcException("robot plugin ev3 has an invalid factory. Check the properties. Server does NOT start", e);
        }
    }
}