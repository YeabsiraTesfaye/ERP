package com.yab.multitenantERP.services;

import com.yab.multitenantERP.config.DynamicSchemaRoutingDataSource;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.TenantRepository;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.spi.*;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemaInitializer {
    private final TenantRepository tenantRepository;
    public SchemaInitializer(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public void createSchema(String schemaName) {
        createTablesInSchema(schemaName);
        String sql = "CREATE SCHEMA " + schemaName;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/erp", "postgres", "root");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);

            Tenant t = new Tenant();
            t.setName(schemaName);
            tenantRepository.save(t);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating schema: " + schemaName, e);
        }
    }

    public void createTablesInSchema(String schemaName) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5432/erp?currentSchema=" + schemaName)
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "root")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.hbm2ddl.auto", "none") // disable auto creation
                .build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(AddressHistory.class)
                .addAnnotatedClass(Attendance.class)
                .addAnnotatedClass(Branch.class)
                .addAnnotatedClass(CompanyWorkSchedule.class)
                .addAnnotatedClass(Department.class)
                .addAnnotatedClass(DepartmentHistory.class)
                .addAnnotatedClass(EmergencyContactHistory.class)
                .addAnnotatedClass(EmployeeWorkSchedule.class)
                .addAnnotatedClass(EmploymentTypeHistory.class)
                .addAnnotatedClass(Holiday.class)
                .addAnnotatedClass(LeaveHistory.class)
                .addAnnotatedClass(Permission.class)
                .addAnnotatedClass(Position.class)
                .addAnnotatedClass(PositionHistory.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(Salary.class)
                .addAnnotatedClass(SalaryHistory.class)
                .addAnnotatedClass(Shift.class)
                .addAnnotatedClass(ShiftAssignment.class)
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(ApiEntity.class)
                .buildMetadata();

        Map<String, Object> optionsMap = new HashMap<>();

        ExecutionOptions executionOptions = new ExecutionOptions() {
            @Override
            public boolean shouldManageNamespaces() {
                return true;
            }
            @Override
            public Map<String, Object> getConfigurationValues() {
                return new HashMap<>();
            }
            @Override
            public ExceptionHandler getExceptionHandler() {
                return exception -> exception.printStackTrace();
            }
        };

        TargetDescriptor targetDescriptor = new TargetDescriptor() {
            @Override
            public EnumSet<TargetType> getTargetTypes() {
                return EnumSet.of(TargetType.DATABASE);
            }

            @Override
            public ScriptTargetOutput getScriptTargetOutput() {
                return null;
            }
        };
        SourceDescriptor sourceDescriptor = new SourceDescriptor() {
            @Override
            public SourceType getSourceType() {
                return SourceType.METADATA;
            }

            @Override
            public ScriptSourceInput getScriptSourceInput() {
                return null;
            }
        };
        SchemaManagementTool schemaManagementTool = registry.getService(SchemaManagementTool.class);
        SchemaCreator schemaCreator = schemaManagementTool.getSchemaCreator(new HashMap<>());
        schemaCreator.doCreation(metadata, executionOptions, ContributableMatcher.ALL, sourceDescriptor, targetDescriptor);
    }

    public List<Tenant> tenants(){
        return tenantRepository.findAll();
    }
}
