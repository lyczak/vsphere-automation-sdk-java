package ga.gaba.EsxiManagementSdk;

import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import vmware.samples.common.SslUtil;
import vmware.samples.common.authentication.VimAuthenticationHelper;

/**
 * Created by glyczak on 11/30/18.
 */
public abstract class AbstractVimAction {
    protected String server;
    protected String username;
    protected String password;
    protected boolean skipServerVerification;
    protected VimAuthenticationHelper vimAuthHelper;
    protected VimPortType vim;
    protected ServiceContent sc;

    public AbstractVimAction(String server, String username, String password, boolean skipServerVerification) {
        this.server = server;
        this.username = username;
        this.password = password;
        this.skipServerVerification = skipServerVerification;
    }

    protected void login() throws Exception {
        this.vimAuthHelper = new VimAuthenticationHelper();
        configureSsl();
        this.vimAuthHelper.loginByUsernameAndPassword(
                this.server, this.username, this.password);

        this.vim = this.vimAuthHelper.getVimPort();
        this.sc = this.vimAuthHelper.getServiceContent();
    }

    protected void configureSsl() throws Exception {
        if(this.skipServerVerification) {
            /*
             * Below method enables all VIM API connections to the server
             * without validating the server certificates.
             *
             * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
             * Circumventing SSL trust is unsafe and should not be used in
             * production software.
             */
            SslUtil.trustAllHttpsCertificates();
        }
    }

    protected void logout() throws Exception {
        this.vimAuthHelper.logout();
    }

    protected static void checkArgs(String args[]) {
        if (args.length < 3 || args.length > 4) {
            System.err.println("Usage: java SomeVimAction <server> <username> <password> [skipServerVerification]");
            System.exit(1);
        }
    }

    protected abstract void run() throws Exception;

    public boolean tryExecute() {
        try {
            execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void execute() throws Exception {
        try {
            login();
            run();
        } finally {
            logout();
        }
    }
}
