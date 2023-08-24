import { NestFactory, HttpAdapterHost } from '@nestjs/core';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import * as swaggerStats from 'swagger-stats';
import { DefaultExceptionsFilter } from './defaultExceptionsFilter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  const config = new DocumentBuilder()
    .setTitle('Logistics example')
    .setDescription('The Logistics API description')
    .setVersion('1.0')
    .addTag('logistics')
    .build();
  const document = SwaggerModule.createDocument(app, config);
  // http://localhost:3000/swagger-stats/#/
  app.use(swaggerStats.getMiddleware({ swaggerSpec: document }));
  SwaggerModule.setup('api', app, document);

  const { httpAdapter } = app.get(HttpAdapterHost);
  app.useGlobalFilters(new DefaultExceptionsFilter(httpAdapter));

  app.enableShutdownHooks();
  await app.listen(3000);
}
bootstrap();
