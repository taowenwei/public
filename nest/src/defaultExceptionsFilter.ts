import { Catch, ArgumentsHost } from '@nestjs/common';
import { BaseExceptionFilter } from '@nestjs/core';

@Catch()
export class DefaultExceptionsFilter extends BaseExceptionFilter {
  catch(exception: unknown, host: ArgumentsHost) {
    const stack =
      exception instanceof Error
        ? exception.stack
        : 'unknown error, no call stack';
    console.log(stack);
    super.catch(exception, host);
  }
}
