package ga.gaba.EsxiManagementSdk.samples;

import com.vmware.vim25.ObjectContent;
import ga.gaba.EsxiManagementSdk.AbstractPropertyRetrievalVimAction;
import ga.gaba.EsxiManagementSdk.DynamicObjectContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/1/18.
 */
public class ListVmVimAction extends AbstractPropertyRetrievalVimAction {
    public ListVmVimAction(String server, String username, String password,
                           boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected void run() throws Exception {
        List<String> props = new ArrayList<>();
        props.add("name");

        List<ObjectContent> objects = retrieveFromRoot("PerformanceManager", props);

        for (ObjectContent content :
                objects) {
            DynamicObjectContent vm = new DynamicObjectContent(content);
            String name = vm.getProp("name");
            System.out.println(name);
        }
    }

    public static void main(String args[]) throws Exception {
        checkArgs(args);
        ListVmVimAction action = new ListVmVimAction(args[0], args[1], args[2],
                ("skipServerVerification".equals(args[3]) || "true".equals(args[3])));
        action.execute();
    }
}
