package ga.gaba.EsxiManagementSdk;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ObjectContent;

/**
 * Created by glyczak on 12/1/18.
 */
public class DynamicObjectContent extends ObjectContent {
    public DynamicObjectContent(ObjectContent o) {
        this.obj = o.getObj();
        this.propSet = o.getPropSet();
        this.missingSet = o.getMissingSet();
    }

    public <T> T getProp(String propertyName) {
        for (DynamicProperty property : this.propSet) {
            if (property.getName().equals(propertyName)) {
                return (T) property.getVal();
            }
        }
        return null;
    }
}
