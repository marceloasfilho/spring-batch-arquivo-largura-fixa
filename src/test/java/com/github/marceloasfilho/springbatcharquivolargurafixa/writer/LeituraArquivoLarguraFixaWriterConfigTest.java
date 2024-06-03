package com.github.marceloasfilho.springbatcharquivolargurafixa.writer;

import com.github.marceloasfilho.springbatcharquivolargurafixa.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SuppressWarnings("unchecked")
@SpringBatchTest
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "spring.batch.job.enabled=false")
public class LeituraArquivoLarguraFixaWriterConfigTest {

    @Mock
    private ItemWriter<Cliente> leituraArquivoLarguraFixaWriter;

    private JobParameters jobParameters;

    @BeforeEach
    public void setUp() {
        this.jobParameters = new JobParametersBuilder()
                .addString("arquivoClientes", "files/clientes.txt")
                .toJobParameters();
    }

    @Test
    public void deveTestarWriterComSucesso() throws Exception {
        // Cenário
        Cliente cliente = new Cliente();
        cliente.setNome("John");
        cliente.setSobrenome("Doe");
        cliente.setIdade("30");
        cliente.setEmail("john@example.com");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(this.jobParameters);

        // Mock
        doAnswer(invocation -> {
            Object chunk = invocation.getArgument(0);
            Assert.isInstanceOf(Chunk.class, chunk);
            Chunk<Cliente> clienteChunk = (Chunk<Cliente>) chunk;
            for (Cliente clienteItem : clienteChunk.getItems()) {
                System.out.println(clienteItem.getNome() + "," + clienteItem.getSobrenome() + "," + clienteItem.getIdade() + "," + clienteItem.getEmail());
            }
            return null;
        }).when(leituraArquivoLarguraFixaWriter).write(any(Chunk.class));

        // Ação
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            leituraArquivoLarguraFixaWriter.write(new Chunk<>(List.of(cliente)));
            return null;
        });

        // Verificação
        assertEquals("John,Doe,30,john@example.com", outputStream.toString().trim());
    }
}
