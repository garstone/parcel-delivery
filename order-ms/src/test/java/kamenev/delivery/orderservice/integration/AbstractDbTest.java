package kamenev.delivery.orderservice.integration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.dbunit.dataset.filter.DefaultColumnFilter.excludedColumnsTable;

@Testcontainers
public abstract class AbstractDbTest extends DataSourceBasedDBTestCase {
    private static final String dbName = "test";
    private static final String userName = "sa";
    private static final String password = "sa";
    private static final String imageName = "postgres:14.3";
    public static final String EMPTY_XML = "src/test/resources/kamenev/delivery/orderservice/integration/empty.xml";
    private IDataSet dataset;
    private static final PostgresqlDataTypeFactory dataTypeFactory = new PostgresqlDataTypeFactory();
    protected final DatasetParameters p = new DatasetParameters();

    @Container
    private static final JdbcDatabaseContainer<?> container = new PostgreSQLContainer(imageName)
            .withDatabaseName(dbName)
            .withUsername(userName)
            .withPassword(password)
            .withInitScript("migration/schema.sql");

    public AbstractDbTest() {
        super();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        System.setProperty("DB_DRIVER", container.getDriverClassName());

        try {
            setupData();
        } catch (Exception ignored) {

        }
    }

    @Override //todo need to solve long time ending test class problem
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        hikariConfig.setMaximumPoolSize(200);
        hikariConfig.setMaxLifetime(SECONDS.toMillis(5));
        hikariConfig.setMinimumIdle(5);
        return new HikariDataSource(hikariConfig);
    }

    @Override
    protected IDataSet getDataSet() {
        return dataset;
    }

    protected void setupData(String xml) throws Exception {
        var flatXmlDataSet = new FlatXmlDataSetBuilder().build(
                Objects.requireNonNull(this.getClass().getResource(xml)));
        dataset = createReplacementDataset(flatXmlDataSet);
        setUp();
    }

    private void setupData() throws Exception {
        dataset = new FlatXmlDataSetBuilder().build(new File(EMPTY_XML));
        setUp();
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        super.setUpDatabaseConfig(config);
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);
    }

    protected void assertData(String xml) throws Exception {
        var actual = createReplacementDataset(getConnection().createDataSet());
        var expected = createReplacementDataset(new FlatXmlDataSetBuilder().build(
                Objects.requireNonNull(this.getClass().getResource(xml))));
        for (ITable expectedTable : expected.getTables()) {
            var tableName = expectedTable.getTableMetaData().getTableName();
            var actualTable = actual.getTable(tableName);
            Assertions.assertNotNull(actualTable);
            Assertion.assertEquals(expectedTable, actualTable);
        }
    }

    protected void assertData(String xml, String... excludedColumns) throws Exception {
        IDataSet actual = getConnection().createDataSet();
        var expected = createReplacementDataset(new FlatXmlDataSetBuilder().build(
                Objects.requireNonNull(this.getClass().getResource(xml))));
        for (ITable expectedTable: expected.getTables()) {
            expectedTable = excludedColumnsTable(expectedTable, excludedColumns);
            var tableName = expectedTable.getTableMetaData().getTableName();
            var actualTable = excludedColumnsTable(actual.getTable(tableName), excludedColumns);
            Assertions.assertNotNull(actualTable);
            Assertion.assertEquals(expectedTable, actualTable);
        }
    }

    private ReplacementDataSet createReplacementDataset(IDataSet dataset) {
        var rds = new ReplacementDataSet(dataset, new HashMap<>(), DatasetParameters.extractDataSetSubstitutions());
        rds.addReplacementObject("[null]", null);
        rds.setStrictReplacement(true);
        rds.setSubstringDelimiters("${", "}");
        return rds;
    }

}
