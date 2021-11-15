
package utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import clustering.Cluster;
import clustering.Couple;
import clustering.Distance;

public class HierarchyBuilder {

    private DistanceMap distances;
    private List<Cluster> clusters;
    private int globalClusterIndex = 0;

    public DistanceMap getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, DistanceMap distances) {
        this.clusters = clusters;
        this.distances = distances;
    }


    public List<Cluster> flatAgg(Linkage linkage, Double threshold)
    {
        while((!isTreeComplete()) && (distances.minDist() != null) && (distances.minDist() <= threshold))
        {
            grouping(linkage);
        }
        return clusters;
    }

    public void grouping(Linkage linkage) {
        Couple minDistLink = distances.removeFirst();
        if (minDistLink != null) {
            clusters.remove(minDistLink.getrCluster());
            clusters.remove(minDistLink.getlCluster());

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(++globalClusterIndex);

            for (Cluster iClust : clusters) {
                Couple link1 = findByClusters(iClust, oldClusterL);
                Couple link2 = findByClusters(iClust, oldClusterR);
                Couple newLinkage = new Couple();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Distance> distanceValues = new ArrayList<Distance>();

                if (link1 != null) {
                    Double distVal = link1.getLinkageDistance();
                    Double weightVal = link1.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link1);
                }
                if (link2 != null) {
                    Double distVal = link2.getLinkageDistance();
                    Double weightVal = link2.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link2);
                }

                Distance newDistance = linkage.calculateDistance(distanceValues);

                newLinkage.setLinkageDistance(newDistance.getDistance());
                distances.add(newLinkage);
            }
            clusters.add(newCluster);
        }
    }

    private Couple findByClusters(Cluster c1, Cluster c2) {
        return distances.findByCodePair(c1, c2);
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

}
