import { Catch, ArgumentsHost, HttpException } from '@nestjs/common';
import { BaseExceptionFilter } from '@nestjs/core';
import { Response } from 'express';

@Catch()
export class DefaultExceptionsFilter extends BaseExceptionFilter {
  catch(exception: HttpException, host: ArgumentsHost) {
    console.log(exception.stack);
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    response.status(exception.getStatus()).json(exception.getResponse());
  }
}
