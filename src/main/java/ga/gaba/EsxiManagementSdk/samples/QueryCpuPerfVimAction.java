package ga.gaba.EsxiManagementSdk.samples;

import com.vmware.vim25.*;
import ga.gaba.EsxiManagementSdk.AbstractPropertyRetrievalVimAction;
import ga.gaba.EsxiManagementSdk.AbstractVimAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/3/18.
 */
public class QueryCpuPerfVimAction extends AbstractPropertyRetrievalVimAction {
    public QueryCpuPerfVimAction(String server, String username, String password,
                                  boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected void run() throws Exception {
        List<PerfQuerySpec> pqSpecSet = new ArrayList<>(1);
        pqSpecSet.add(new PerfQuerySpec());
        PerfQuerySpec pqSpec = pqSpecSet.get(0);

        List<ObjectContent> hosts = retrieveFromRoot("HostSystem", new ArrayList<String>());
        pqSpec.setEntity(hosts.get(0).getObj());
        //pqSpec.setFormat("csv");

        PerfMetricId pmId = new PerfMetricId();
        pmId.setCounterId(4);
        pmId.setInstance("");
        pqSpec.getMetricId().add(pmId);

        List<PerfEntityMetricBase> metrics = vim.queryPerf(sc.getPerfManager(), pqSpecSet);

        PerfEntityMetric metric = (PerfEntityMetric) metrics.get(0);
        PerfMetricIntSeries series = (PerfMetricIntSeries) metric.getValue().get(0);
        for (Long v :
                series.getValue()) {
            System.out.println(v);
        }
    }

    public static void main(String args[]) throws Exception {
        checkArgs(args);
        QueryCpuPerfVimAction action = new QueryCpuPerfVimAction(args[0], args[1], args[2],
                ("skipServerVerification".equals(args[3]) || "true".equals(args[3])));
        action.execute();
    }
}

