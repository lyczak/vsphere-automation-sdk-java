package ga.gaba.EsxiManagementSdk.samples;

import com.vmware.vim25.HostListSummaryQuickStats;
import com.vmware.vim25.ObjectContent;
import ga.gaba.EsxiManagementSdk.AbstractPropertyRetrievalVimAction;
import ga.gaba.EsxiManagementSdk.DynamicObjectContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/2/18.
 */
public class ListHostStatsVimAction extends AbstractPropertyRetrievalVimAction {
    public ListHostStatsVimAction(String server, String username, String password,
                                  boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected void run() throws Exception {
        List<String> props = new ArrayList<>();
        props.add("summary.quickStats");

        List<ObjectContent> objects = retrieveFromRoot("HostSystem", props);

        DynamicObjectContent host = new DynamicObjectContent(objects.get(0));
        HostListSummaryQuickStats quickStats = host.getProp("summary.quickStats");
        System.out.println(String.format("%.2f GHz Overall CPU Usage", quickStats.getOverallCpuUsage() / 1E3));
        System.out.println(String.format("%.2f GB Overall Memory Usage", quickStats.getOverallMemoryUsage() / 1E3));
        System.out.println(String.format("%.2f Days Uptime", quickStats.getUptime() / 8.64E4));
    }

    public static void main(String args[]) throws Exception {
        checkArgs(args);
        ListHostStatsVimAction action = new ListHostStatsVimAction(args[0], args[1], args[2],
                ("skipServerVerification".equals(args[3]) || "true".equals(args[3])));
        action.execute();
    }
}
