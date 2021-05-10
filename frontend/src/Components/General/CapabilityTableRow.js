import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import deleteImg from '../../img/delete.jpg'

export default class CapabilityTableRow extends Component {

    render() {
        return (
            <tr>
                <td>
                    { this.props.obj.capabilityId}
                </td>
                <td>
                    <Link to={ /environment/ + this.props.obj.environment.environmentName + '/capability/' + this.props.obj.capabilityId+ '/edit' }>{ this.props.obj.capabilityName }</Link>
                </td>
                <td>
                    <Link to={ '/delete/' + this.props.obj.capabilityName }><img src={ deleteImg } alt='delete' width='15' height='18'/></Link>&nbsp;&nbsp;
                </td>
            </tr>
        )
    }
}
