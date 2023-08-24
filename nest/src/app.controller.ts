import { Controller, Get, HttpException, HttpStatus } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getHello() {
    // test runtime exception
    throw new HttpException('Dummy', HttpStatus.INTERNAL_SERVER_ERROR);
    // or try use,
    // throw 'dummy';
  }
}
