package utility;

import java.util.*;

import clustering.Cluster;
import clustering.Couple;


public class DistanceMap {

    private Map<String, Item> pairHash;
    private PriorityQueue<Item> data;

    private class Item implements Comparable<Item> {
        final Couple pair;
        final String hash;
        boolean removed = false;

        Item(Couple p) {
            pair = p;
            hash = hashCodePair(p);
        }

        @Override
        public int compareTo(Item o) {
            return pair.compareTo(o.pair);
        }

        @Override
        public String toString() {
            return hash;
        }
    }

    public DistanceMap() {
        data = new PriorityQueue<Item>();
        pairHash = new HashMap<String, Item>();
    }

    public List<Couple> list() {
        List<Couple> l = new ArrayList<Couple>(data.size());
        for (Item clusterPair : data) {
            l.add(clusterPair.pair);
        }
        return l;
    }

    public Couple findByCodePair(Cluster c1, Cluster c2) {
        String inCode = hashCodePair(c1, c2);
        return pairHash.get(inCode).pair;
    }

    public Couple removeFirst() {
        Item poll = data.poll();
        while (poll != null && poll.removed) {
            poll = data.poll();
        }
        if (poll == null) {
            return null;
        }
        Couple link = poll.pair;
        pairHash.remove(poll.hash);
        return link;
    }

    public boolean remove(Couple link) {
        Item remove = pairHash.remove(hashCodePair(link));
        if (remove == null) {
            return false;
        }
        remove.removed = true;
        return true;
    }


    public boolean add(Couple link) {
        Item e = new Item(link);
        Item existingItem = pairHash.get(e.hash);
        if (existingItem != null) {
            System.err.println("hashCode = " + existingItem.hash +
                    " adding redundant link:" + link + " (exist:" + existingItem + ")");
            return false;
        } else {
            pairHash.put(e.hash, e);
            data.add(e);
            return true;
        }
    }

    public Double minDist()
    {
        Item peek = data.peek();
        if (peek != null)
            return peek.pair.getLinkageDistance();
        else
            return null;
    }

   
    private String hashCodePair(Couple link) {
        return hashCodePair(link.getlCluster(), link.getrCluster());
    }

    private String hashCodePair(Cluster lCluster, Cluster rCluster) {
        return hashCodePairNames(lCluster.getName(), rCluster.getName());
    }

    private String hashCodePairNames(String lName, String rName) {
        if (lName.compareTo(rName) < 0) {
            return lName + "-" + rName;
        } else {
            return rName + "-" + lName;
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
