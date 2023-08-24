import { Controller, Get, HttpException, HttpStatus } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get('ping')
  ping(): string {
    return this.appService.getHello();
  }

  @Get('eping')
  eping(): string {
    throw new HttpException('test exception', HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
