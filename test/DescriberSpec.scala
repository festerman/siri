import java.io.BufferedWriter

import models.Describer
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.mockito.Mockito.when

/**
 * Created by staffan on 2015-03-16.
 */

@RunWith(classOf[JUnitRunner])
class DescriberSpec extends Specification with Mockito {

  val m = spy(scala.io.Source)
  // Mockito.doReturn().when(m.fromFile(anyString))

  "A Describer" should {
    "load OK if file is OK" in {

    }
  }
}
