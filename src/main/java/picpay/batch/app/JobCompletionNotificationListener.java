package picpay.batch.app;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) return;
        log.info("!!! JOB FINISHED! Time to verify the results");
        final String query = "SELECT first_name, last_name FROM people";
        jdbcTemplate.query(query, (rs, row) -> new Person(rs.getString(1), rs.getString(2)))
                .forEach(person -> log.info(String.format("Found <%s> in the database.", person)));
    }
}
