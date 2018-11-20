import React from 'react';
import styles from './AccordionItem.module.css';
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

  getClassName(openId: string | undefined) {
    const result = [styles.accordionItem];
    openId === this.props.id && result.push(styles.open);
    this.props.success && result.push(styles.success);
    return result.join(' ')
  }

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
            <div className={this.getClassName(openItemId)}>
              {this.props.children}
            </div>
          </AccordionItemContext.Provider>
        )}
      </AccordionContext.Consumer>
    )
  }
}