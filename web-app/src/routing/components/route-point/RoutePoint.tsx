import React from 'react';
import styles from './RoutePoint.module.css';

import { ICheckpoint } from '../../models/route';

import { AccordionItem, AccordionItemTitle, AccordionItemBody, Divider, NameValue } from '../../../ui';
import { ICompletedCheckpoint } from '../../models/route-instance';
import { IPerson } from '../../models/person';
import { formatTime, formatMinutes } from '../../utils/date';

interface RoutePointProps {
  point: ICheckpoint;
  pointInstance?: ICompletedCheckpoint;
  person: IPerson;
}

export class RoutePoint extends React.Component<RoutePointProps> {

  render() {
    const { point, pointInstance } = this.props;

    return (
      <AccordionItem id={point.id} success={!!pointInstance}>
        <AccordionItemTitle>
          {point.name}
        </AccordionItemTitle>
        <AccordionItemBody>
          {({ isOpen }) => (
            isOpen
              ? <RoutePointFullInfo {...this.props} />
              : <RoutePointShortInfo {...this.props} />
          )}
        </AccordionItemBody>
      </AccordionItem>
    );
  }
}

interface RoutePointInfoProps {
  point: ICheckpoint;
  pointInstance?: ICompletedCheckpoint;
  person: IPerson;
}

const RoutePointShortInfo: React.StatelessComponent<RoutePointInfoProps> =
  ({ point, pointInstance }) => (
    <div className={styles.row}>
      <div className={styles.col}>
        <NameValue name='Прибытие' value={formatTime(pointInstance && pointInstance.arrival)} />
        <NameValue name='Отправление' value={formatTime(pointInstance && pointInstance.departure)} />
      </div>
      <div className={styles.col}>
        <NameValue name='План' value={formatMinutes(point.planTime)} />
        <NameValue name='Факт' value={formatMinutes(pointInstance && pointInstance.factTime)} />
      </div>
    </div>
  );

const RoutePointFullInfo: React.StatelessComponent<RoutePointInfoProps> =
  ({ point, pointInstance, person }) => (
    <>
      <RoutePointShortInfo point={point} pointInstance={pointInstance} person={person} />
      <Divider />
      <NameValue name='Адрес' value={point.address} vertical={true} />
      <Divider />
      <NameValue name='Супервайзер' value={[person.lastName, person.firstName, person.middleName].join(' ')} />
      <Divider />
      <NameValue name='Описание' value={point.description} vertical={true} />
    </>
  );
