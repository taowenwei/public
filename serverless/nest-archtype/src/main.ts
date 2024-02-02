import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { initSwaggeropenApi } from './utils/swaggerUtils';
import { DefaultExceptionsFilter } from './utils/defaultExceptionsFilter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();
  initSwaggeropenApi(app);
  app.useGlobalFilters(new DefaultExceptionsFilter());
  app.enableShutdownHooks();
  await app.listen(3000);
}
bootstrap();
