import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { LogisticsController } from './logistics.controller';
import { LogisticsService, ModelName } from './logistics.service';
import { LogisticsSchema } from './logistics.schema';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: ModelName, schema: LogisticsSchema }]),
  ],
  controllers: [LogisticsController],
  providers: [LogisticsService],
})
export class LogisticsModule {}
