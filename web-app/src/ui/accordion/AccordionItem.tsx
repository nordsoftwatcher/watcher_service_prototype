import React from 'react';
import styles from './AccordionItem.module.css';

import cn from 'classnames'

import { AccordionContext } from './Accordion'

export interface AccordionItemContext {
  toggle: () => void;
  isOpen: boolean;
  isSuccess: boolean;
}

export const AccordionItemContext = React.createContext<AccordionItemContext>({
  toggle: () => { },
  isOpen: false,
  isSuccess: false,
})

export interface AccordionItemProps {
  id: string;
  success?: boolean;
}

export default class AccordionItem extends React.Component<AccordionItemProps> {

  render() {
    const { id } = this.props;
    const isOpen = (openItemId: string | undefined) => openItemId === id;

    return (
      <AccordionContext.Consumer>
        {({ openItemId, toggle }) => (
          <AccordionItemContext.Provider value={{
            isOpen: isOpen(openItemId),
            toggle: () => toggle(id, !isOpen(openItemId)),
            isSuccess: !!this.props.success,
          }}>
            <div className={cn({
              [styles.accordionItem]: true,
              [styles.open]: openItemId === this.props.id,
              [styles.success]: this.props.success,
            })}>
              {this.props.children}
            </div>
          </AccordionItemContext.Provider>
        )}
      </AccordionContext.Consumer>
    )
  }
}