import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'logistics' })
export class Logistics {
  @Prop()
  'Order ID': string;

  @Prop()
  'Order Date': string;

  @Prop()
  'Origin Port': string;

  @Prop()
  'Carrier': string;

  @Prop()
  'TPT': string;

  @Prop()
  'Service Level': string;

  @Prop()
  'Ship ahead day count': string;

  @Prop()
  'Ship Late Day count': string;

  @Prop()
  'Customer': string;

  @Prop()
  'Product ID': string;

  @Prop()
  'Plant Code': string;

  @Prop()
  'Destination Port': string;

  @Prop()
  'Unit quantity': string;

  @Prop()
  'Weight': string;
}

export const LogisticsSchema = SchemaFactory.createForClass(Logistics);
export type LogisticsDocument = Logistics & Document;
