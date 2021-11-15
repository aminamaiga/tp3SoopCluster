
package utility;

import java.util.Collection;

import clustering.Distance;

public class SingleLinkageStrategy implements Linkage {

	@Override
	public Distance calculateDistance(Collection<Distance> distances) {
		double min = Double.NaN;

		for (Distance dist : distances) {
		    if (Double.isNaN(min) || dist.getDistance() < min)
		        min = dist.getDistance();
		}
		return new Distance(min);
	}
}
