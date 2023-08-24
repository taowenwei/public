import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Logistics, LogisticsDocument } from './logistics.schema';
import { Model } from 'mongoose';

export const ModelName = 'Logistics';

@Injectable()
export class LogisticsService {
  private readonly logger = new Logger(LogisticsService.name);

  constructor(
    @InjectModel(ModelName) private logisticsModel: Model<LogisticsDocument>,
  ) {}

  async findOne(query: object): Promise<Logistics> {
    this.logger.log(`query parameters ${JSON.stringify(query)}`);
    return this.logisticsModel.findOne(query).exec();
  }
}
