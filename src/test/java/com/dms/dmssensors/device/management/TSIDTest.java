package com.dms.dmssensors.device.management;

import com.dms.dmssensors.device.management.common.IdGenerate;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class TSIDTest {
     @Test
     void testGenerateTSID() {
         TSID tsid = IdGenerate.generateTSID();

         System.out.println(String.format("TSID gerado em: %s", tsid.getInstant()));

         Assertions.assertThat(tsid.getInstant())
                 .isCloseTo(Instant.now(), Assertions.within(1, ChronoUnit.MINUTES));
     }
}
