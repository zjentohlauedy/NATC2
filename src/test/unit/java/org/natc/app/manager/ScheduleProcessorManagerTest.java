package org.natc.app.manager;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.processor.ScheduleProcessor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleProcessorManagerTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private Map<ScheduleType, String> scheduleTypeProcessorMap;

    @InjectMocks
    private ScheduleProcessorManager scheduleProcessorManager;

    @Nested
    class GetProcessorFor {

        @Test
        public void shouldGetScheduleProcessorBeanFromApplicationContext() {
            when(scheduleTypeProcessorMap.get(any(ScheduleType.class))).thenReturn("bean-name");

            scheduleProcessorManager.getProcessorFor(ScheduleType.AWARDS);

            verify(context).getBean(anyString(), eq(ScheduleProcessor.class));
        }

        @Test
        public void shouldGetBeanFromApplicationContextBasedOnScheduleType() {
            final String expectedBeanName = "my-bean-name";
            final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            when(scheduleTypeProcessorMap.get(ScheduleType.AWARDS)).thenReturn(expectedBeanName);

            scheduleProcessorManager.getProcessorFor(ScheduleType.AWARDS);

            verify(context).getBean(captor.capture(), eq(ScheduleProcessor.class));

            final String actualBeanName = captor.getValue();

            assertEquals(expectedBeanName, actualBeanName);
        }

        @Test
        public void shouldReturnTheScheduleProcessorFoundInTheApplicationContext() {
            final ScheduleProcessor expectedScheduleProcessor = mock(ScheduleProcessor.class);

            when(scheduleTypeProcessorMap.get(ScheduleType.AWARDS)).thenReturn("bean-name");
            when(context.getBean("bean-name", ScheduleProcessor.class)).thenReturn(expectedScheduleProcessor);

            final ScheduleProcessor actualScheduleProcessor = scheduleProcessorManager.getProcessorFor(ScheduleType.AWARDS);

            assertSame(expectedScheduleProcessor, actualScheduleProcessor);
        }
    }
}