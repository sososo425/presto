/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.cost;

import com.facebook.presto.Session;
import com.facebook.presto.matching.Pattern;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.sql.planner.Symbol;
import com.facebook.presto.sql.planner.iterative.Lookup;
import com.facebook.presto.sql.planner.plan.PlanNode;
import com.facebook.presto.sql.planner.plan.SemiJoinNode;

import java.util.Map;
import java.util.Optional;

public class SemiJoinStatsRule
        implements ComposableStatsCalculator.Rule
{
    private static final Pattern<SemiJoinNode> PATTERN = Pattern.typeOf(SemiJoinNode.class);

    @Override
    public Pattern<SemiJoinNode> getPattern()
    {
        return PATTERN;
    }

    @Override
    public Optional<PlanNodeStatsEstimate> calculate(PlanNode node, StatsProvider statsProvider, Lookup lookup, Session session, Map<Symbol, Type> types)
    {
        SemiJoinNode semiJoinNode = (SemiJoinNode) node;
        PlanNodeStatsEstimate sourceStats = statsProvider.getStats(semiJoinNode.getSource());

        // For now we just propagate statistics for source symbols.
        // Handling semiJoinOutput symbols requires support for correlation for boolean columns.

        return Optional.of(sourceStats);
    }
}
