package ga.gaba.EsxiManagementSdk.samples;

import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ObjectContent;
import ga.gaba.EsxiManagementSdk.AbstractPropertyRetrievalVimAction;
import ga.gaba.EsxiManagementSdk.DynamicObjectContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/2/18.
 */
public class ListDatastoreVimAction extends AbstractPropertyRetrievalVimAction {
    public ListDatastoreVimAction(String server, String username, String password,
                                       boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected void run() throws Exception {
        List<String> props = new ArrayList<>();
        props.add("summary");

        List<ObjectContent> objects = retrieveFromRoot("Datastore", props);

        for (ObjectContent objectContent :
                objects) {
            DynamicObjectContent ds = new DynamicObjectContent(objectContent);
            DatastoreSummary quickStats = ds.getProp("summary");
            System.out.println(String.format("%s:", quickStats.getName()));
            System.out.println(String.format("%.2f GB Available out of %.2f GB Total",
                    quickStats.getFreeSpace() / 1E9, quickStats.getCapacity() / 1E9));
            System.out.println();
        }
    }

    public static void main(String args[]) throws Exception {
        checkArgs(args);
        ListDatastoreVimAction action = new ListDatastoreVimAction(args[0], args[1], args[2],
                ("skipServerVerification".equals(args[3]) || "true".equals(args[3])));
        action.execute();
    }
}
