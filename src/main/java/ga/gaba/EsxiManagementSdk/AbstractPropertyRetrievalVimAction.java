package ga.gaba.EsxiManagementSdk;

import com.vmware.vim25.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glyczak on 12/1/18.
 */
public abstract class AbstractPropertyRetrievalVimAction extends AbstractVimAction {
    public AbstractPropertyRetrievalVimAction(String server,
                                              String username, String password,
                                              boolean skipServerVerification) {
        super(server, username, password, skipServerVerification);
    }

    protected List<ObjectContent> retrieveFrom(String type, ManagedObjectReference from,
                                                   List<String> properties) throws Exception {
        List<PropertyFilterSpec> pfSpecSet = new ArrayList<>(1);
        pfSpecSet.add(new PropertyFilterSpec());
        PropertyFilterSpec pfSpec = pfSpecSet.get(0);

        pfSpec.getPropSet().add(new PropertySpec());
        PropertySpec propSpec = pfSpec.getPropSet().get(0);
        propSpec.setType(type);
        propSpec.setAll(false);
        propSpec.getPathSet().addAll(properties);

        pfSpec.getObjectSet().add(new ObjectSpec());
        ObjectSpec objSpec = pfSpec.getObjectSet().get(0);
        objSpec.setObj(from);

        RetrieveResult propResponse = vim.retrievePropertiesEx(
                sc.getPropertyCollector(), pfSpecSet, new RetrieveOptions());

        return propResponse.getObjects();
    }

    protected List<ObjectContent> retrieveFromRoot(String type,
                                                   List<String> properties) throws Exception {
        List<String> cvList = new ArrayList<>(1);
        cvList.add(type);
        ManagedObjectReference containerView = vim.createContainerView(
                sc.getViewManager(), sc.getRootFolder(), cvList, true);

        List<PropertyFilterSpec> pfSpecSet = new ArrayList<>(1);
        pfSpecSet.add(new PropertyFilterSpec());
        PropertyFilterSpec pfSpec = pfSpecSet.get(0);

        pfSpec.getPropSet().add(new PropertySpec());
        PropertySpec propSpec = pfSpec.getPropSet().get(0);
        propSpec.setType(type);
        propSpec.setAll(false);
        propSpec.getPathSet().addAll(properties);

        pfSpec.getObjectSet().add(new ObjectSpec());
        ObjectSpec objSpec = pfSpec.getObjectSet().get(0);
        objSpec.setObj(containerView);
        objSpec.getSelectSet().add(new TraversalSpec());
        TraversalSpec tvSpec = (TraversalSpec) objSpec.getSelectSet().get(0);
        tvSpec.setPath("view");
        tvSpec.setType("ContainerView");
        tvSpec.setSkip(false);

        RetrieveResult propResponse = vim.retrievePropertiesEx(
                sc.getPropertyCollector(), pfSpecSet, new RetrieveOptions());

        return propResponse.getObjects();
    }
}
