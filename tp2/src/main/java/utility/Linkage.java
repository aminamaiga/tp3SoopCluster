
package utility;

import java.util.Collection;

import clustering.Distance;

public interface Linkage {

    public Distance calculateDistance(Collection<Distance> distances);
}
