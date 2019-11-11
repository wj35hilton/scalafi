package io.scalafi

import cats.effect.{ ExitCode, IO }
import io.chrisdavenport.log4cats.Logger
import java.io.InputStream
import org.apache.nifi.components.{ PropertyDescriptor, PropertyValue }
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.processor.{ AbstractProcessor, ProcessContext, ProcessSession, Relationship }
import com.videologygroup.scalafi._

abstract class LoggerAccess extends AbstractProcessor { protected def log = getLogger }

// TODO: is trait best here ... some sort of closed class maybe?
trait IOProc extends LoggerAccess {

  import scala.jdk.CollectionConverters._

  // TODO: don't like using implicits here ... need some other way to manage the 'context'/'session'/'log'
  def run(flowFile: FlowFile)(
    implicit S: NifiProcessSession[IO], C: NifiProcessContext[IO], L: Logger[IO]): IO[ExitCode]

  override def onTrigger(context: ProcessContext,  session: ProcessSession): Unit = Option(session.get()) match {
    case Some(f) => {
      // TODO: take a look a IOApp and IOAppPlatform for mgmt of thread contexts etc
      //       -- right now we just unsafeRunSync
      val _ = run(f)(

        new NifiProcessSession[IO] {
          def transfer(f: FlowFile, r: Relationship): IO[Unit] =
            IO.delay(session.transfer(f, r))
          def openInputStream(f: FlowFile): IO[InputStream] =
            IO.delay(session.read(f))
          def setAttribute(f: FlowFile, attrName: String, attrValue: String): IO[FlowFile] =
            IO.delay(session.putAttribute(f, attrName, attrValue))

          // TODO: see if this can be removed after refactoring recordreader junk
          def unsafeNiFiLog = log
        },

        new NifiProcessContext[IO] {
          def dynamicProperties: IO[List[PropertyDescriptor]] =
            IO.delay(context.getProperties.keySet().asScala.toList.filter(_.isDynamic))
          def getProperty(d: PropertyDescriptor): IO[PropertyValue] =
            IO.delay(context.getProperty(d))
        },

        NifiLogger.impl[IO](log)).unsafeRunSync()
      ()
    }
    case _ =>
  }
}
