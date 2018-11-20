import React from 'react';
import styles from './RoutePoint.module.css';

import { IRoutePoint } from '../../models/route';

import { AccordionItem, AccordionItemTitle, AccordionItemBody, Divider, NameValue } from '../../../ui';
import { IRouteInstacePoint } from '../../models/route-instance';
import { IPerson } from '../../models/person';

interface RoutePointProps {
  point: IRoutePoint;
  pointInstance?: IRouteInstacePoint;
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
          {state => {
            if (state.isOpen) {
              return <RoutePointFullInfo {...this.props} />
            } else {
              return <RoutePointShortInfo {...this.props} />
            }
          }}
        </AccordionItemBody>
      </AccordionItem>
    )
  }
}

interface RoutePointInfoProps {
  point: IRoutePoint;
  pointInstance?: IRouteInstacePoint;
  person: IPerson;
}

const RoutePointShortInfo: React.StatelessComponent<RoutePointInfoProps> =
  ({ point, pointInstance }) => (
    <div className={styles.row}>
      <div className={styles.col}>
        <NameValue name='Прибытие' value={pointInstance && pointInstance.arrival || '-'}></NameValue>
        <NameValue name='Отправление' value={pointInstance && pointInstance.departure || '-'}></NameValue>
      </div>
      <div className={styles.col}>
        <NameValue name='План' value='-'></NameValue>
        <NameValue name='Факт' value='-'></NameValue>
      </div>
    </div>
  )

const RoutePointFullInfo: React.StatelessComponent<RoutePointInfoProps> =
  ({ point, pointInstance, person }) => (
    <>
      <RoutePointShortInfo point={point} pointInstance={pointInstance} person={person} />
      <Divider />
      <NameValue name='Адрес' value={point.address} vertical />
      <Divider />
      <NameValue name='Супервайзер' value={[person.lastName, person.firstName, person.middleName].join(' ')} />
      <Divider />
      <NameValue name='Описание' value={point.description} vertical />
    </>
  )

