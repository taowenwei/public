import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { LogisticsModule } from './logistics/logistics.module';
import { MongooseModule } from '@nestjs/mongoose';
import { HealthModule } from './health/health.module';
import { LoggerMiddleware } from './loggerMiddleware';

@Module({
  imports: [
    MongooseModule.forRoot(process.env.DBURL),
    LogisticsModule,
    HealthModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(LoggerMiddleware).forRoutes('*');
  }
}
