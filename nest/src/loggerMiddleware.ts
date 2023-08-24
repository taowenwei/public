import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';

@Injectable()
export class LoggerMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    const { baseUrl, params, query, headers, body } = req;
    const sub = { baseUrl, params, query, headers, body };
    console.log(`${JSON.stringify(sub)}`);
    next();
  }
}
