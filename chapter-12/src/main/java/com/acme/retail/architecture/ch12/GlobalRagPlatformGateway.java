package com.acme.retail.architecture.ch12;

import java.util.List;
import java.util.Map;

/**
 * The actual composed design: every request passes through tenant
 * isolation (Chapter 6), is answered by the correct region's sharded
 * index (Chapter 5), and is attributed to the correct deployment
 * location (Chapter 7) -- three independently real mechanisms, composed
 * into one platform gateway.
 */
public class GlobalRagPlatformGateway {

    private final NoisyNeighborGuard tenantGuard;
    private final Map<String, ShardedDocumentIndex> indexByRegion;
    private final GlobalDeploymentRouter deploymentRouter;

    public GlobalRagPlatformGateway(
            NoisyNeighborGuard tenantGuard,
            Map<String, ShardedDocumentIndex> indexByRegion,
            GlobalDeploymentRouter deploymentRouter) {
        this.tenantGuard = tenantGuard;
        this.indexByRegion = Map.copyOf(indexByRegion);
        this.deploymentRouter = deploymentRouter;
    }

    public GlobalQueryResult handle(GlobalQueryRequest request) {
        IsolationDecision isolation = tenantGuard.evaluate(
                new ResourceUsageRequest(request.tenantId(), request.resourceUnits()));

        if (!isolation.allowed()) {
            return new GlobalQueryResult(false, isolation.reason(), List.of(), 0, null);
        }

        ShardedDocumentIndex regionalIndex = indexByRegion.get(request.region());
        if (regionalIndex == null) {
            throw new IllegalArgumentException("No regional index registered for region: " + request.region());
        }

        ShardSearchResult searchResult = regionalIndex.searchByCategory(request.category(), request.queryTerm());
        String servedByLocation = deploymentRouter.targetFor(request.tenantId(), request.region()).locationName();

        return new GlobalQueryResult(
                true, isolation.reason(), searchResult.matches(), searchResult.shardsSearched(), servedByLocation);
    }
}
