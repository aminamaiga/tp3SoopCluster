

package clustering;

public class Couple implements Comparable<Couple> {

    private Cluster lCluster;
    private Cluster rCluster;
    private Double linkageDistance;

    public Couple(){
    }

    public Couple(Cluster left, Cluster right, Double distance) {
        lCluster = left;
        rCluster = right;
        linkageDistance = distance;
    }

    public Cluster getOtherCluster(Cluster c) {
    return lCluster == c ? rCluster : lCluster;
  }

    public Cluster getlCluster() {
        return lCluster;
    }

    public void setlCluster(Cluster lCluster) {
        this.lCluster = lCluster;
    }

    public Cluster getrCluster() {
        return rCluster;
    }

    public void setrCluster(Cluster rCluster) {
        this.rCluster = rCluster;
    }

    public Double getLinkageDistance() {
        return linkageDistance;
    }

    public void setLinkageDistance(Double distance) {
        this.linkageDistance = distance;
    }

    /**
     * @return a new ClusterPair with the two left/right inverted
     */
    public Couple reverse() {
        return new Couple(getrCluster(), getlCluster(), getLinkageDistance());
    }



    @Override
    public int compareTo(Couple o) {
        int result;
        if (o == null || o.getLinkageDistance() == null) {
            result = -1;
        } else if (getLinkageDistance() == null) {
            result = 1;
        } else {
            result = getLinkageDistance().compareTo(o.getLinkageDistance());
        }

        return result;
    }

    public Cluster agglomerate(int clusterIdx) {
        return agglomerate("clstr#" + clusterIdx);
    }

    public Cluster agglomerate(String name) {
        Cluster cluster = new Cluster(name);
        cluster.setDistance(new Distance(getLinkageDistance()));
        cluster.appendLeafNames(lCluster.getLeafNames());
        cluster.appendLeafNames(rCluster.getLeafNames());
        cluster.addChild(lCluster);
        cluster.addChild(rCluster);
        lCluster.setParent(cluster);
        rCluster.setParent(cluster);

        Double lWeight = lCluster.getWeightValue();
        Double rWeight = rCluster.getWeightValue();
        double weight = lWeight + rWeight;
        cluster.getDistance().setWeight(weight);

        return cluster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (lCluster != null) {
            sb.append(lCluster.getName());
        }
        if (rCluster != null) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append(rCluster.getName());
        }
        sb.append(" : ").append(linkageDistance);
        return sb.toString();
    }

}
