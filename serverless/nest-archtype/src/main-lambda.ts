import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { initSwaggeropenApi } from './utils/swaggerUtils';
import { DefaultExceptionsFilter } from './utils/defaultExceptionsFilter';
import serverlessExpress from '@vendia/serverless-express';
import { Callback, Context, Handler } from 'aws-lambda';

let server: Handler;

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  initSwaggeropenApi(app);
  app.useGlobalFilters(new DefaultExceptionsFilter());
  app.enableShutdownHooks();

  await app.init();
  const expressApp = app.getHttpAdapter().getInstance();
  return serverlessExpress({ app: expressApp });
}

export const handler: Handler = async (
  event: any,
  context: Context,
  callback: Callback,
) => {
  // https://about.lovia.life/docs/engineering/nestjs/deploy-nestjs-express-with-serverless-framework-on-aws-lambda/
  // https://github.com/vendia/serverless-express/issues/86
  event.path = `/${event.pathParameters['ANY']}`;
  if (event.path === '/api') {
    event.path = '/api/';
  }
  event.path = event.path.includes('swagger-ui')
    ? `/api${event.path}`
    : event.path;
  console.log(JSON.stringify(event, null, 2));
  server = server ?? (await bootstrap());
  return server(event, context, callback);
};
