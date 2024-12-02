package appeng.me.service;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridServiceProvider;
import appeng.api.networking.pathing.ChannelMode;
import appeng.api.networking.pathing.ControllerState;
import appeng.api.networking.pathing.IPathingService;
import net.minecraft.nbt.CompoundTag;

public class GraphPathingService implements IPathingService, IGridServiceProvider {
    Map<IGridNode, Vertex> node2Vertex = new HashMap<>();
    Set<Vertex> vertices = new HashSet<>();
    
    public void addNode(IGridNode gridNode, @Nullable CompoundTag savedData) {
        for (IGridConnection connection : gridNode.getConnections()) {
            IGridNode other = connection.getOtherSide(gridNode);
            Vertex connected = node2Vertex.get(other);
            // connected to vertex already in the graph
            if (connected != null) {
                if (other.getConnections().size() != 2) {
                    // branch
                }
                connected.nodes.add(gridNode);
            }
        }
    }
    
    @Override
    public boolean isNetworkBooting() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNetworkBooting'");
    }

    @Override
    public ControllerState getControllerState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getControllerState'");
    }

    @Override
    public void repath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'repath'");
    }

    @Override
    public ChannelMode getChannelMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChannelMode'");
    }

    @Override
    public int getUsedChannels() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsedChannels'");
    }

    /**
     * nodes is the grid nodes contained in this vertex, outgoing is the
     * outgoing edges from this vertex, incoming is the incoming edges to this
     * vertex (used for efficently pathing away from the that something's
     * changed (is this even needed?)), weight is the total weight of this
     * vertex (distance from controller)
     *
     * Incoming is towards the controller, outgoing is away
     */   
    private record Vertex(Deque<IGridNode> nodes, Set<Edge> outgoing, Set<Edge> incoming, int weight) {
        private void addNode(IGridNode connected, IGridNode newNode) {
            if (nodes.peekFirst() == connected) {
                nodes.addFirst(newNode);
            } else if (nodes.peekLast() == connected) {
                nodes.addLast(newNode);
            } else {
                // this shouldn't happen, code should have followed a different
                // path to split the vertex up
                throw new IllegalArgumentException("Pathing Service attempted to add a node into the middle of a vertex!");
            }
        }

        /**
         * Split this vertex into 2 pieces along the given node
         *
         * Splits into two distinct vertices and assigns the new node to the
         * shorter pathed vertex
         *
         * @return the vertex the node was assigned to
         */
        private Vertex split(IGridNode splittingNode) {
            nodes.descendingIterator()
        }
    }

    private record Edge(Vertex to, IGridConnection connection) {}
}
