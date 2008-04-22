package javax.xml.bind.annotation.adapters;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class XmlAdapter<ValueType, BoundType> {

    protected XmlAdapter() {
    }

    public abstract ValueType marshal(BoundType v);

    public abstract BoundType unmarshal(ValueType v);
}
