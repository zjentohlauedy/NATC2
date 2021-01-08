package org.natc.app.manager;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.generator.ScheduleDataGenerator;
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
class ScheduleDataGeneratorManagerTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private Map<ScheduleType, String> scheduleTypeDataGeneratorMap;

    @InjectMocks
    private ScheduleDataGeneratorManager manager;

    @Nested
    class GetGeneratorFor {

        @Test
        public void shouldGetScheduleDataGeneratorBeanFromApplicationContext() {
            when(scheduleTypeDataGeneratorMap.get(any(ScheduleType.class))).thenReturn("anything");

            manager.getGeneratorFor(ScheduleType.PRESEASON);

            verify(context).getBean(anyString(), eq(ScheduleDataGenerator.class));
        }

        @Test
        public void shouldGetBeanFromApplicationContextBasedOnScheduleType() {
            final String expectedBeanName = "my-bean-name";
            final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            when(scheduleTypeDataGeneratorMap.get(ScheduleType.PRESEASON)).thenReturn(expectedBeanName);

            manager.getGeneratorFor(ScheduleType.PRESEASON);

            verify(context).getBean(captor.capture(), eq(ScheduleDataGenerator.class));

            final String actualBeanName = captor.getValue();

            assertEquals(expectedBeanName, actualBeanName);
        }

        @Test
        public void shouldReturnTheScheduleDataGeneratorFoundInTheApplicationContext() {
            final ScheduleDataGenerator expectedGenerator = mock(ScheduleDataGenerator.class);

            when(scheduleTypeDataGeneratorMap.get(any(ScheduleType.class))).thenReturn("anything");
            when(context.getBean("anything", ScheduleDataGenerator.class)).thenReturn(expectedGenerator);

            final ScheduleDataGenerator actualGenerator = manager.getGeneratorFor(ScheduleType.PRESEASON);

            assertSame(expectedGenerator, actualGenerator);
        }
    }
}