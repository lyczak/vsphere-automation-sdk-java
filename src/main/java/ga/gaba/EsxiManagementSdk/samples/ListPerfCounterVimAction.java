package ga.gaba.EsxiManagementSdk.samples;

import com.vmware.vim25.*;
import ga.gaba.EsxiManagementSdk.AbstractPropertyRetrievalVimAction;
import ga.gaba.EsxiManagementSdk.DynamicObjectContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/4/18.
 */
public class ListPerfCounterVimAction extends AbstractPropertyRetrievalVimAction {
    public ListPerfCounterVimAction(String server, String username, String password,
                                  boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected void run() throws Exception {
        List<String> props = new ArrayList<>();
        props.add("perfCounter");

        List<ObjectContent> objects = retrieveFrom("PerformanceManager", sc.getPerfManager(), props);

        List<PerfCounterInfo> perfCounters = new ArrayList<>();

        for (ObjectContent content : objects) {
            List<DynamicProperty> dps = content.getPropSet();
            if (dps != null) {
                for (DynamicProperty dp : dps) {
                    perfCounters = ((ArrayOfPerfCounterInfo) dp.getVal()).getPerfCounterInfo();
                }
            }
        }

        for (PerfCounterInfo counter :
                perfCounters) {
            Integer counterId = counter.getKey();
            String counterGroup = counter.getGroupInfo().getKey();
            String counterName = counter.getNameInfo().getKey();
            String counterRollupType = counter.getRollupType().toString();
            String fullCounterName = counterGroup + "." + counterName + "." + counterRollupType;
            System.out.println(fullCounterName + " = " + counterId);
        }
    }

    public static void main(String args[]) throws Exception {
        checkArgs(args);
        ListPerfCounterVimAction action = new ListPerfCounterVimAction(args[0], args[1], args[2],
                ("skipServerVerification".equals(args[3]) || "true".equals(args[3])));
        action.execute();
    }
}

