import { Controller, Get, Query } from '@nestjs/common';
import { LogisticsService } from './logistics.service';
import { Logistics } from './logistics.schema';

@Controller('logistics')
export class LogisticsController {
  constructor(private readonly logisticsService: LogisticsService) {}

  @Get('findOne')
  async findOne(@Query() query): Promise<Logistics> {
    return this.logisticsService.findOne(query);
  }
}
