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
package com.facebook.presto.kafka;

import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.connector.ConnectorFactory;
import com.facebook.presto.spi.connector.ConnectorFactoryContext;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Presto plugin to use Apache Kafka as a data source.
 */
public class KafkaPlugin
        implements Plugin
{
    private Optional<Supplier<Map<SchemaTableName, KafkaTopicDescription>>> tableDescriptionSupplier = Optional.empty();

    @VisibleForTesting
    public synchronized void setTableDescriptionSupplier(Supplier<Map<SchemaTableName, KafkaTopicDescription>> tableDescriptionSupplier)
    {
        this.tableDescriptionSupplier = Optional.of(requireNonNull(tableDescriptionSupplier, "tableDescriptionSupplier is null"));
    }

    @Override
    public synchronized Iterable<ConnectorFactory> getConnectorFactories(ConnectorFactoryContext context)
    {
        return ImmutableList.of(new KafkaConnectorFactory(
                context.getTypeManager(),
                context.getNodeManager(),
                tableDescriptionSupplier));
    }
}
