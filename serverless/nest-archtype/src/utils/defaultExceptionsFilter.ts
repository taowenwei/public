import { Catch, ArgumentsHost, HttpException } from '@nestjs/common';
import { BaseExceptionFilter } from '@nestjs/core';
import { Response } from 'express';

@Catch()
export class DefaultExceptionsFilter extends BaseExceptionFilter {
  catch(exception: unknown, host: ArgumentsHost) {
    const stack =
      exception instanceof Error
        ? exception.stack
        : 'unknown error, no call stack';
    console.log(stack);
    const status =
      exception instanceof HttpException ? exception.getStatus() : 500;
    const msg =
      exception instanceof Error ? exception.message : 'unknown error';

    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    console.error({ status, msg });
    response.status(status).json({ status, msg });
  }
}
