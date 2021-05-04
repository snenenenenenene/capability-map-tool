import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import deleteImg from '../img/delete.jpg'

export default class RecentEnvironmentTableRow extends Component {

    deleteEnvironment (){
        const response = fetch(`http://localhost:8080/environment/delete/${this.props.obj.environmentId}`, {
            method: "DELETE"
        });
        if (!response.ok) {
            console.log('Failed to delete environment');
        }
        console.log(`Environment deleted with key: ${this.props.obj.environmentId}`);
    }

    render() {
        return (
            <tr>
                <td>
                    { this.props.obj.environmentId }
                </td>

                <td>
                    <Link to={ '/environment/' + this.props.obj.environmentName }>

                    { this.props.obj.environmentName }
        </Link>
                </td>
                <td>
                    <img src={ deleteImg } onClick={() => {this.deleteEnvironment(this.props.obj.environmentId)}} alt='delete' width='15' height='18'/>
                </td>
            </tr>

        )
    }
}
