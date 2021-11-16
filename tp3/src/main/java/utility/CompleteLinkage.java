
package utility;

import java.util.Collection;

import clustering.Distance;

public class CompleteLinkage implements Linkage {

	@Override
	public Distance calculateDistance(Collection<Distance> distances) {
		double max = Double.NaN;

		for (Distance dist : distances) {
			if (Double.isNaN(max) || dist.getDistance() > max)
				max = dist.getDistance();
		}
		return new Distance(max);
	}
}
