import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import MaterialTable from 'material-table';
import './GeneralTable.css'
import axios from 'axios';
import toast from 'react-hot-toast';

export default class Strategy extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            strategies: [],
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => this.setState({environmentId: response.data.environmentId}))
        .catch(error => {
            this.props.history.push('/notfounderror')
        })
        
        await axios.get(`${process.env.REACT_APP_API_URL}/strategy/`)
        .then(response => {
                this.setState({strategies: response.data});
            })
        .catch(error => {
            toast.error('Could Not Find Strategies')
        })
    }

    edit(strategyId){
        this.props.history.push(`/environment/${this.state.environmentName}/strategy/${strategyId}`)
    }
    
    fetchDeleteStrategies = async(strategyId) => {
        await axios.delete(`${process.env.REACT_APP_API_URL}/strategy/${strategyId}`)
        .then(response => toast.success("Succesfully Deleted Strategy"))
        .catch(error => toast.error("Could not Delete Strategy"))
        //REFRESH Strategies
        await axios.get(`${process.env.REACT_APP_API_URL}/strategy/all-strategies-by-environmentid/${this.state.environmentId}`)
        .then(response => {
                this.setState({strategies: []})
                this.setState({strategies: response.data});
            })
        .catch(error => {
            toast.error("Could not Find Strategies")
        })
    }

    delete = async(strategyId) => {
        toast((t) => (
            <span>
                <p className="text-center">Are you sure you want to remove this strategy?</p>
                <div className="text-center">
            <button className="btn btn-primary btn-sm m-3" stlye={{width: 50, height:30}} onClick={() => 
            {
                toast.dismiss(t.id)
                this.fetchDeleteStrategies(strategyId)
            }}>
                Yes!
              </button>
              <button className="btn btn-secondary btn-sm m-3" stlye={{width: 50, height:30}} onClick={() => toast.dismiss(t.id)}>
                No!
              </button>
              </div>
            </span>
          ), {duration: 50000})
        }
    
    render() {
        return(
            <div>
            <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
                <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}/strategy`}>Strategy</Link></li>
            </ol>
        </nav>
            <div className="jumbotron">
                <div>
                    <h1 className='display-4' style={{display: 'inline-block'}}>Strategies</h1>
                    <Link to={`/environment/${this.state.environmentName}/strategy/add`}><button className="btn btn-primary float-right">Add Strategy</button></Link>
                </div>
                <br/><br/>
                <MaterialTable
            columns={[
              
            { title: "ID", field: "strategyId" },
            { title: "Name", field: "strategyName" },
            { title: "Start", field: "timeFrameStart" },
            { title: "End", field: "timeFrameEnd" },
            { title: "Environment", field: "status.environmentId"},
            {
                title: '', 
                name: 'delete',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.delete.bind(this, rowData.strategyId)} className="bi bi-trash"></i></button>
            },
            {
                title: '', 
                name: 'edit',
                render: rowData => <button className="btn btn-secondary"><i onClick={this.edit.bind(this, rowData.strategyId)} className="bi bi-pencil"></i></button>
            },
          ]}
          data={this.state.strategies}
          options={{
              showTitle: false,
              search: false
          }}
        />
        </div>
        </div>
        )
    }
}