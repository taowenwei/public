import { INestApplication } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';

const PROJECT_NAME = '{YOUR-PROJECT-NAME}';

export const initSwaggeropenApi = (app: INestApplication) => {
  const config = new DocumentBuilder()
    .setTitle(PROJECT_NAME)
    .setDescription(`${PROJECT_NAME} API description`)
    .setVersion('1.0')
    .addTag(PROJECT_NAME)
    .build();
  const document = SwaggerModule.createDocument(app, config);
  // MUST use "@nestjs/swagger": "^5.2.1", the latest "^6.0.1" has issue
  SwaggerModule.setup('api', app, document);
};
