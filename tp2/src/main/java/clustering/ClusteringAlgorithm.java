
package clustering;

import java.util.*;

import utility.DistanceMap;
import utility.HierarchyBuilder;
import utility.Linkage;

public class ClusteringAlgorithm  
{

    public Cluster performClustering(double[][] distances,
                                     String[] clusterNames, Linkage linkage)
    {

        checkArguments(distances, clusterNames, linkage);
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
        {
            builder.grouping(linkage);
        }

        return builder.getRootCluster();
    }

    public List<Cluster> performFlatClustering(double[][] distances,
                                               String[] clusterNames, Linkage linkage, Double threshold)
    {

        checkArguments(distances, clusterNames, linkage);
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        return builder.flatAgg(linkage, threshold);
    }

    private void checkArguments(double[][] distances, String[] clusterNames,
                                Linkage linkage)
    {
        if (distances == null || distances.length == 0
                || distances[0].length != distances.length)
        {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
        if (distances.length != clusterNames.length)
        {
            throw new IllegalArgumentException("Invalid cluster name array");
        }
        if (linkage == null)
        {
            throw new IllegalArgumentException("Undefined linkage strategy");
        }
        int uniqueCount = new HashSet<String>(Arrays.asList(clusterNames)).size();
        if (uniqueCount != clusterNames.length)
        {
            throw new IllegalArgumentException("Duplicate names");
        }
    }

    public Cluster performWeightedClustering(double[][] distances, String[] clusterNames,
                                             double[] weights, Linkage linkage)
    {

        checkArguments(distances, clusterNames, linkage);

        if (weights.length != clusterNames.length)
        {
            throw new IllegalArgumentException("Invalid weights array");
        }

        /* Setup model */
        List<Cluster> clusters = createClusters(clusterNames, weights);
        DistanceMap linkages = createLinkages(distances, clusters);

        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete())
        {
            builder.grouping(linkage);
        }

        return builder.getRootCluster();
    }

    private DistanceMap createLinkages(double[][] distances,
                                       List<Cluster> clusters)
    {
        DistanceMap linkages = new DistanceMap();
        for (int col = 0; col < clusters.size(); col++)
        {
            for (int row = col + 1; row < clusters.size(); row++)
            {
                Couple link = new Couple();
                Cluster lCluster = clusters.get(col);
                Cluster rCluster = clusters.get(row);
                link.setLinkageDistance(distances[col][row]);
                link.setlCluster(lCluster);
                link.setrCluster(rCluster);
                linkages.add(link);
            }
        }
        return linkages;
    }

    private List<Cluster> createClusters(String[] clusterNames)
    {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (String clusterName : clusterNames)
        {
            Cluster cluster = new Cluster(clusterName);
            cluster.addLeafName(clusterName);
            clusters.add(cluster);
        }
        return clusters;
    }

    private List<Cluster> createClusters(String[] clusterNames, double[] weights)
    {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (int i = 0; i < weights.length; i++)
        {
            Cluster cluster = new Cluster(clusterNames[i]);
            cluster.setDistance(new Distance(0.0, weights[i]));
            clusters.add(cluster);
        }
        return clusters;
    }

}
